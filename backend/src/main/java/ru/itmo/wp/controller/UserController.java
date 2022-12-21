package ru.itmo.wp.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.exception.ValidationException;
import ru.itmo.wp.form.RegisterCredentials;
import ru.itmo.wp.form.validator.RegisterCredentialsEnterValidator;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;
    private final RegisterCredentialsEnterValidator registerCredentialsEnterValidator;

    public UserController(JwtService jwtService, UserService userService, RegisterCredentialsEnterValidator registerCredentialsEnterValidator) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.registerCredentialsEnterValidator = registerCredentialsEnterValidator;
    }

    @InitBinder("registerCredentials")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(registerCredentialsEnterValidator);
    }

    @GetMapping("users/auth")
    public User findUserByJwt(@RequestParam String jwt) {
        return jwtService.find(jwt);
    }

    @GetMapping("users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("users")
    public String register(@RequestBody @Valid RegisterCredentials registerCredentials, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return jwtService.create(userService.register(registerCredentials));
    }
}
