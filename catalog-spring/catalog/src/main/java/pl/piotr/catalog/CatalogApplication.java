package pl.piotr.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.piotr.catalog.domain.recipe.RecipeService;
import pl.piotr.catalog.domain.token.TokenService;
import pl.piotr.catalog.domain.user.UserService;

@SpringBootApplication
public class CatalogApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

}
