package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.RegisterCredentials;
import ru.itmo.wp.service.UserService;

@Component
public class RegisterCredentialsEnterValidator implements Validator {
    private final UserService userService;

    public RegisterCredentialsEnterValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class<?> clazz) {
        return RegisterCredentials.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            RegisterCredentials enterForm = (RegisterCredentials) target;
            if (userService.findByLogin(enterForm.getLogin()) != null) {
                errors.reject("login-already-in-use", "Login already in use");
            }
        }
    }
}
