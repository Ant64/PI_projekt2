package pl.piotr.catalog.domain.user;

import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.piotr.catalog.domain.exception.DomainException;
import pl.piotr.catalog.domain.exception.ExceptionCode;
import pl.piotr.catalog.domain.token.Token;
import pl.piotr.catalog.domain.token.TokenService;
import pl.piotr.catalog.lib.Assertion;
import pl.piotr.types.api.TokenView;

import java.time.Clock;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Clock clock;
    private final ConversionService conversionService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, Clock clock, ConversionService conversionService, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.clock = clock;
        this.conversionService = conversionService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
    }

    public void createUser(String login, String password){
        UserValidator.validate(login, password);

        boolean accountAlreadyExist = userRepository.existsByLogin(login);

        if(accountAlreadyExist) throw new DomainException(ExceptionCode.LOGIN_NOT_UNIQUE, login);

        String hashedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User(
                UUID.randomUUID().toString(),
                login,
                hashedPassword,
                Role.STANDARD,
                clock.instant()
        );

        userRepository.save(user);
    }

    public TokenView loginUser(String login, String password){
        boolean userExist = userRepository.existsByLogin(login);

        if(!userExist) throw new DomainException(ExceptionCode.INVALID_CREDENTIALS, login);

        User user = userRepository.getByLogin(login)
                .orElseThrow(() -> new DomainException(ExceptionCode.INVALID_CREDENTIALS, login));

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) throw new DomainException(ExceptionCode.INVALID_CREDENTIALS, login);

        Token token = tokenService.createToken(user);

        return conversionService.convert(token, TokenView.class);
    }

    public void logoutUser(String token) {
        tokenService.deleteToken(token);
    }

    private static class UserValidator{
        public static void validate(String login, String password){
           Assertion.isTrue(!Objects.isNull(login) && login.length() > 3, () -> new DomainException(ExceptionCode.INVALID_LOGIN, login));
           Assertion.isTrue(!Objects.isNull(password) && password.length() > 3, () -> new DomainException(ExceptionCode.INVALID_PASSWORD, login));
        }
    }
}
