package pl.piotr.catalog.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.piotr.catalog.domain.token.Token;
import pl.piotr.types.api.TokenView;

@Component
public class TokenToTokenViewConverter implements Converter<Token, TokenView> {

    @Override
    public TokenView convert(Token token) {
        TokenView tokenView = new TokenView();
        tokenView.setLogin(token.getUser().getLogin());
        tokenView.setToken(token.getValue());
        tokenView.setRole(token.getUser().getRole().toString());
        return tokenView;
    }
}
