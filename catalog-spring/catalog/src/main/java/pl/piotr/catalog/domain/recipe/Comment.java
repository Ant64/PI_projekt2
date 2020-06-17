package pl.piotr.catalog.domain.recipe;

import pl.piotr.catalog.domain.recipe.Recipe;
import pl.piotr.catalog.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    private String commentId;
    @ManyToOne
    private User user;
    private Instant createdAt;
    private String content;

    public Comment() {
    }

    public Comment(String commentId, User user, Instant createdAt, String content) {
        this.commentId = commentId;
        this.user = user;
        this.createdAt = createdAt;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }

    public String getCommentId() {
        return commentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}
