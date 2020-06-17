package pl.piotr.catalog.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByLogin(String login);
    Optional<User> getByLogin(String login);
}
