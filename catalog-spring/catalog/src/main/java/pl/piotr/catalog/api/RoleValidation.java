package pl.piotr.catalog.api;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.piotr.catalog.domain.token.TokenService;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RoleValidation {
    private HttpServletRequest request;
    private final TokenService tokenService;

    public RoleValidation(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Before("@annotation(pl.piotr.catalog.api.RequireModeratorRole)")
    public void requireAdminAuthenticate() {
        initializeServletRequest();
        String token = request.getHeader("token");
        tokenService.validateIfUserIsModerator(token);
    }

    private void initializeServletRequest(){
        request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
