package pl.piotr.catalog.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.piotr.catalog.domain.recipe.DifficultyPerforming;
import pl.piotr.catalog.domain.recipe.DishType;
import pl.piotr.catalog.domain.recipe.Image;
import pl.piotr.catalog.domain.recipe.RecipeService;
import pl.piotr.types.api.CreateCommentRequest;
import pl.piotr.types.api.CreateRecipeRequest;
import pl.piotr.types.api.RecipeView;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/create")
    public RecipeView createRecipe(@RequestHeader("token") String token, @RequestBody CreateRecipeRequest request){
        return recipeService.createRecipe(
                token,
                request.getTitle(),
                request.getContent(),
                request.getDifficultyPerforming(),
                request.getDishType(),
                request.getDishIngredients(),
                request.getHoursDuration(),
                request.getMinutesDuration()
        );
    }

    @PostMapping("/{recipeId}/upload-image")
    public RecipeView uploadImage(@PathVariable String recipeId, @RequestParam("image") MultipartFile file, @RequestHeader("token") String token){
        return recipeService.uploadImage(recipeId, file, token);
    }

    @GetMapping("/{recipeId}/get-image")
    public Image getImage(@PathVariable String recipeId){
        return recipeService.getImage(recipeId);
    }

    @GetMapping("/{recipeId}/get")
    public RecipeView getRecipe(@PathVariable String recipeId){
        return recipeService.getRecipeViewById(recipeId);
    }

    @GetMapping("/get-all")
    public List<RecipeView> getAll(
            @RequestParam(value = "dishType", required = false) DishType dishType,
            @RequestParam(value = "difficultyPerforming", required = false) DifficultyPerforming difficultyPerforming,
            @RequestParam(value = "user", required = false) String username
            ){
        return recipeService.getAllRecipes(dishType, difficultyPerforming, username);
    }

    @PostMapping("/{recipeId}/comment/add")
    public RecipeView addComment(@RequestHeader("token") String token, @RequestBody CreateCommentRequest request, @PathVariable String recipeId){
        return recipeService.addComment(
                token,
                request.getContent(),
                recipeId
        );
    }
    
    @RequireModeratorRole
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable String commentId){
        recipeService.deleteComment(commentId);
    }
}
