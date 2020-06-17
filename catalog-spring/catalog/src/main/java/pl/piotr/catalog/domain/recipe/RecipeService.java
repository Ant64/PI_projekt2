package pl.piotr.catalog.domain.recipe;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.piotr.catalog.domain.exception.DomainException;
import pl.piotr.catalog.domain.exception.ExceptionCode;
import pl.piotr.catalog.domain.token.TokenService;
import pl.piotr.catalog.domain.user.User;
import pl.piotr.catalog.lib.Assertion;
import pl.piotr.types.api.RecipeView;

import java.io.IOException;
import java.time.Clock;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final Clock clock;
    private final TokenService tokenService;
    private final ConversionService conversionService;
    private final CommentRepository commentRepository;

    public RecipeService(RecipeRepository recipeRepository, Clock clock, TokenService tokenService, ConversionService conversionService, CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.clock = clock;
        this.tokenService = tokenService;
        this.conversionService = conversionService;
        this.commentRepository = commentRepository;
    }

    public RecipeView createRecipe(String token, String title, String content, DifficultyPerforming difficultyPerforming,
                                   DishType dishType, List<DishIngredient> dishIngredient, Double hoursDuration, Double minutesDuration) {
        RecipeValidator.validate(title, content, hoursDuration, minutesDuration);

        User user = tokenService.getUserByToken(token);

        Recipe recipe = new Recipe(
                UUID.randomUUID().toString(),
                clock.instant(),
                user,
                title,
                content,
                difficultyPerforming,
                dishType,
                new HashSet<>(dishIngredient),
                hoursDuration,
                minutesDuration,
                new ArrayList<>());

        recipeRepository.save(recipe);

        return conversionService.convert(recipe, RecipeView.class);
    }

    public RecipeView uploadImage(String recipeId, MultipartFile file, String token) {

        Recipe recipe = getRecipeByRecipeId(recipeId);

        User user = tokenService.getUserByToken(token);

        RecipeValidator.validate(user, recipe.getUser());

        Image image;

        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            if (fileName.contains("..")) {
                throw new DomainException(ExceptionCode.INVALID_IMAGE_FILENAME);
            }

            image = new Image(fileName, file.getContentType(), file.getBytes());
        } catch (Exception e) {
            throw new DomainException(ExceptionCode.ERROR_PARSING_IMAGE);
        }

        recipe.uploadImage(image);

        recipeRepository.save(recipe);

        return conversionService.convert(recipe, RecipeView.class);
    }

    public Image getImage(String recipeId) {
        Recipe recipe = getRecipeByRecipeId(recipeId);

        Image image = recipe.getImage();

        RecipeValidator.validate(image);

        return image;
    }

    public RecipeView addComment(String token, String content, String recipeId) {
        RecipeValidator.validate(content);
        User user = tokenService.getUserByToken(token);
        Recipe recipe = getRecipeByRecipeId(recipeId);

        Comment comment = new Comment(
                UUID.randomUUID().toString(),
                user,
                clock.instant(),
                content
        );

        recipe.getComments().add(comment);

        recipe.setComments(deleteToFiveTheOldest(recipe.getComments()));

        recipeRepository.save(recipe);

        return conversionService.convert(recipe, RecipeView.class);
    }


    public List<RecipeView> getAllRecipes(DishType dishType, DifficultyPerforming difficultyPerforming, String username) {
        Predicate<Recipe> predicateDishType = pr -> true;
        Predicate<Recipe> predicateDifficultyPerforming = pr -> true;
        Predicate<Recipe> predicateUsername = pr -> true;

        if (!Objects.isNull(dishType)) predicateDishType = pr -> pr.getDishType().equals(dishType);
        if (!Objects.isNull(difficultyPerforming))
            predicateDifficultyPerforming = pr -> pr.getDifficultyPerforming().equals(difficultyPerforming);
        if (!Objects.isNull(username)) {
            if (!username.isEmpty())
                predicateUsername = pr -> pr.getUser().getLogin().equals(username);
        }

        return recipeRepository.findAll().stream()
                .filter(predicateDishType)
                .filter(predicateDifficultyPerforming)
                .filter(predicateUsername)
                .map(recipe -> conversionService.convert(recipe, RecipeView.class))
                .collect(Collectors.toList());
    }

    public Recipe getRecipeByRecipeId(String recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new DomainException(ExceptionCode.NO_SUCH_RECIPE, recipeId));
    }

    private List<Comment> deleteToFiveTheOldest(List<Comment> comments) {
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DomainException(ExceptionCode.NO_SUCH_COMMENT, commentId));

        Recipe recipe = recipeRepository.findAll().stream()
                .filter(recipe1 -> recipe1.getComments().contains(comment))
                .findAny()
                .orElseThrow(() -> new DomainException(ExceptionCode.INTERNAL_SERVER));

        recipe.getComments().remove(comment);

        recipeRepository.save(recipe);
    }

    public RecipeView getRecipeViewById(String recipeId) {
        Recipe recipe = getRecipeByRecipeId(recipeId);

        return conversionService.convert(recipe, RecipeView.class);
    }

    private static class RecipeValidator {
        static void validate(String title, String content, Double hoursDuration, Double minutesDuration) {
            Assertion.isTrue(!Objects.isNull(title) && title.length() >= 5,
                    () -> new DomainException(ExceptionCode.INVALID_TITLE, title));
            Assertion.isTrue(!Objects.isNull(content) && content.length() >= 10,
                    () -> new DomainException(ExceptionCode.INVALID_CONTENT_RECIPE, content));
            Assertion.isTrue(!Objects.isNull(hoursDuration) && !Objects.isNull(minutesDuration) && hoursDuration < 24 && minutesDuration < 60,
                    () -> {
                        String hoursDurationString = hoursDuration == null ? "null" : hoursDuration.toString();
                        String minutesDurationString = minutesDuration == null ? "null" : minutesDuration.toString();
                        return new DomainException(ExceptionCode.INVALID_DISH_DURATION, hoursDurationString, minutesDurationString);
                    });
        }

        static void validate(String content) {
            Assertion.isTrue(!Objects.isNull(content) && content.length() > 10,
                    () -> new DomainException(ExceptionCode.INVALID_CONTENT_COMMENT, content));
        }

        static void validate(User user1, User user2) {
            Assertion.isTrue(user1.getUserId().equals(user2.getUserId()), () -> new DomainException(ExceptionCode.INVALID_LOGIN));
        }

        static void validate(Image image) {
            Assertion.notNull(image, () -> new DomainException(ExceptionCode.NO_SUCH_IMAGE));
        }
    }
}
