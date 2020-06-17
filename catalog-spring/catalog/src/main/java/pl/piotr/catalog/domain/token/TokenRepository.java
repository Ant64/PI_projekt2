package pl.piotr.catalog.domain.token;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.piotr.catalog.domain.user.User;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {
    List<Token> findAllByUser(User user);
}
