package pl.piotr.catalog.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.piotr.catalog.domain.exception.DomainException;
import pl.piotr.catalog.domain.exception.ExceptionCode;
import pl.piotr.catalog.domain.user.UserService;
import pl.piotr.types.api.CreateUserRequest;
import pl.piotr.types.api.TokenView;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public void createUser(@RequestBody CreateUserRequest request){
        userService.createUser(
                request.getLogin(),
                request.getPassword()
        );
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public TokenView login(@RequestParam Map<String, String> paramMap){
        if(paramMap == null || !paramMap.containsKey("login") || !paramMap.containsKey("password")){
            throw new DomainException(ExceptionCode.ERROR_PARSING_REQUEST);
        }

        return userService.loginUser(
                paramMap.get("login"),
                paramMap.get("password")
        );
    }

    @GetMapping("/logout")
    public void logout(@RequestHeader("token") String token){
        userService.logoutUser(token);
    }
}
