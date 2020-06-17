package pl.piotr.catalog.domain.token;

import pl.piotr.catalog.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Token {
    @Id
    private String value;
    @OneToOne
    private User user;

    public Token() {
    }

    public Token(String value, User user) {
        this.value = value;
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", user=" + user +
                '}';
    }
}
