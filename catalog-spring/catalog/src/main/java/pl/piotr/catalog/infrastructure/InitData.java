package pl.piotr.catalog.infrastructure;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.piotr.catalog.domain.recipe.RecipeService;
import pl.piotr.catalog.domain.user.Role;
import pl.piotr.catalog.domain.user.User;
import pl.piotr.catalog.domain.user.UserRepository;
import pl.piotr.catalog.domain.user.UserService;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.UUID;

@Component
public class InitData {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Clock clock;
    private UserRepository userRepository;

    public InitData(BCryptPasswordEncoder bCryptPasswordEncoder, Clock clock, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.clock = clock;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        String hashedPassword = bCryptPasswordEncoder.encode("admin");

        User user = new User(
                UUID.randomUUID().toString(),
                "admin",
                hashedPassword,
                Role.MODERATOR,
                clock.instant()
        );

        userRepository.save(user);
    }
}
