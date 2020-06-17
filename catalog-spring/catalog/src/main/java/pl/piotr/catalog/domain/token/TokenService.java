package pl.piotr.catalog.domain.token;

import org.springframework.stereotype.Service;
import pl.piotr.catalog.domain.exception.DomainException;
import pl.piotr.catalog.domain.exception.ExceptionCode;
import pl.piotr.catalog.domain.user.Role;
import pl.piotr.catalog.domain.user.User;

import java.util.List;
import java.util.UUID;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(User user){
        List<Token> alreadyCreatedTokenForSpecificUser = tokenRepository.findAllByUser(user);

        alreadyCreatedTokenForSpecificUser.forEach(tokenRepository::delete);

        Token token = new Token(UUID.randomUUID().toString(), user);

        return tokenRepository.save(token);
    }

    public void deleteToken(String token){
        tokenRepository.findById(token)
                .ifPresent(tokenRepository::delete);
    }

    public User getUserByToken(String token){
        return tokenRepository.findById(token)
                .orElseThrow(() -> new DomainException(ExceptionCode.INVALID_TOKEN, token))
                .getUser();
    }

    public void validateIfUserIsModerator(String token) {
        User user = getUserByToken(token);
        if(user.getRole() != Role.MODERATOR) {
            throw new DomainException(ExceptionCode.NOT_SUFFICIENT_PERMISSIONS, user.getLogin());
        }
    }
}
