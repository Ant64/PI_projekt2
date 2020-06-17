package pl.piotr.catalog.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import pl.piotr.catalog.domain.recipe.Comment;
import pl.piotr.types.api.CommentView;

import java.time.format.DateTimeFormatter;

@Service
public class CommentToCommentViewConverter implements Converter<Comment, CommentView> {
    private final DateTimeFormatter dateTimeFormatter;

    public CommentToCommentViewConverter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public CommentView convert(Comment comment) {
        CommentView commentView = new CommentView();
        commentView.setContent(comment.getContent());
        commentView.setCreatedAt(dateTimeFormatter.format(comment.getCreatedAt()));
        commentView.setUser(comment.getUser().getLogin());
        commentView.setCommentId(comment.getCommentId());

        return commentView;
    }
}
