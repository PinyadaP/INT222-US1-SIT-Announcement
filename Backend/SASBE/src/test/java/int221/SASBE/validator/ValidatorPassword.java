package int221.SASBE.validator;

import int221.SASBE.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmlunit.validation.ValidationResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ValidatorPassword implements ConstraintValidator<ValidPassword, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // Check if the input is blank
        if (s == null || s.trim().isEmpty()) {
            addConstraintViolation("must not be blank", constraintValidatorContext);
            return false;
        }

        // Validate password complexity
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 14),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
        ));
//        return validator.validate(new PasswordData(s)).isValid();


        RuleResult validationResult = validator.validate(new PasswordData(s));
        if (!validationResult.isValid()) {
            addConstraintViolation("must be 8-14 characters long, at least 1 of uppercase, lowercase, number, and special characters", constraintValidatorContext);
            return false;
        }

        return true;
    }

    private void addConstraintViolation(String message, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

}

