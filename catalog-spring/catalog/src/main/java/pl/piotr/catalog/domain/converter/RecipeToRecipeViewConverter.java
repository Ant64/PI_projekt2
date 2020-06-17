package pl.piotr.catalog.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import pl.piotr.catalog.domain.recipe.DishIngredient;
import pl.piotr.catalog.domain.recipe.Recipe;
import pl.piotr.types.api.CommentView;
import pl.piotr.types.api.RecipeView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeToRecipeViewConverter implements Converter<Recipe, RecipeView> {
    private final DateTimeFormatter formatter;

    public RecipeToRecipeViewConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public RecipeView convert(Recipe recipe) {
        RecipeView recipeView = new RecipeView();

        recipeView.setContent(recipe.getContent());
        recipeView.setTitle(recipe.getTitle());
        recipeView.setCreatedAt(formatter.format(recipe.getCreatedAt()));
        recipeView.setDifficultyPerforming(recipe.getDifficultyPerforming().getTranslatedToPolish());
        recipeView.setDishType(recipe.getDishType().getTranslatedToPolish());
        recipeView.setUser(recipe.getUser().getLogin());
        recipeView.setRecipeId(recipe.getRecipeId());
        recipeView.setMinutesDuration(recipe.getMinutesDuration());
        recipeView.setHoursDuration(recipe.getHoursDuration());

        List<CommentView> comments = recipe.getComments().stream()
                .map(comment -> {
                    CommentView commentView = new CommentView();
                    commentView.setCommentId(comment.getCommentId());
                    commentView.setContent(comment.getContent());
                    commentView.setCreatedAt(formatter.format(comment.getCreatedAt()));
                    commentView.setUser(comment.getUser().getLogin());

                    return commentView;
                })
                .collect(Collectors.toList());
        recipeView.setComments(comments);

        List<String> dishIngredients = recipe.getDishIngredient().stream()
                .map(DishIngredient::getTranslatedToPolish)
                .collect(Collectors.toList());
        recipeView.setDishIngredients(dishIngredients);

        return recipeView;
    }
}
