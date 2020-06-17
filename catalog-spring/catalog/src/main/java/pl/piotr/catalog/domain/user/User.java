package pl.piotr.catalog.domain.user;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class User {
    @Id
    private String userId;
    @Column(unique = true)
    private String login;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Instant createdAt;

    public User() {
    }

    public User(String userId, String login, String password, Role role, Instant createdAt) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void updateRole(Role role){
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }
}
