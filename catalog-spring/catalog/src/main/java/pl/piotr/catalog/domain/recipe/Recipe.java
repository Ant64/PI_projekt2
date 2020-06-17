package pl.piotr.catalog.domain.recipe;

import pl.piotr.catalog.domain.user.User;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
public class Recipe {
    @Id
    private String recipeId;
    private Instant createdAt;
    @ManyToOne
    private User user;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private DifficultyPerforming difficultyPerforming;
    @Enumerated(EnumType.STRING)
    private DishType dishType;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DishIngredient> dishIngredient;
    private Double hoursDuration;
    private Double minutesDuration;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    public Recipe() {
    }

    public Recipe(String recipeId, Instant createdAt, User user, String title, String content, DifficultyPerforming difficultyPerforming,
                  DishType dishType, Set<DishIngredient> dishIngredient, Double hoursDuration, Double minutesDuration, List<Comment> comments) {
        this.recipeId = recipeId;
        this.createdAt = createdAt;
        this.user = user;
        this.title = title;
        this.content = content;
        this.difficultyPerforming = difficultyPerforming;
        this.dishType = dishType;
        this.dishIngredient = dishIngredient;
        this.hoursDuration = hoursDuration;
        this.minutesDuration = minutesDuration;
        this.comments = comments;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public DifficultyPerforming getDifficultyPerforming() {
        return difficultyPerforming;
    }

    public DishType getDishType() {
        return dishType;
    }

    public Set<DishIngredient> getDishIngredient() {
        return dishIngredient;
    }

    public Double getHoursDuration() {
        return hoursDuration;
    }

    public Double getMinutesDuration() {
        return minutesDuration;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", difficultyPerforming=" + difficultyPerforming +
                ", dishType=" + dishType +
                ", dishIngredient=" + dishIngredient +
                ", hoursDuration='" + hoursDuration + '\'' +
                ", minutesDuration='" + minutesDuration + '\'' +
                '}';
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Image getImage() {
        return image;
    }

    public void uploadImage(Image image){
        this.image = image;
    }
}
