package int221.SASBE.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;


public class ValidatorPassword implements ConstraintValidator<ValidPassword, String>  {
    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            context.buildConstraintViolationWithTemplate("must not be blank")
                    .addConstraintViolation();
            return false;
        }


        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 14),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
        ));
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        // Clear existing constraint violations
        context.disableDefaultConstraintViolation();

        // Handle each failed rule individually
        for (RuleResultDetail detail : result.getDetails()) {
            switch (detail.getErrorCode()) {
                case "TOO_SHORT":
                    context.buildConstraintViolationWithTemplate("size must be between 8 and 14")
                            .addConstraintViolation();
                    break;
                case "TOO_LONG":
                case "INSUFFICIENT_LOWERCASE":
                case "INSUFFICIENT_UPPERCASE":
                case "INSUFFICIENT_DIGIT":
                case "INSUFFICIENT_SPECIAL":
                    context.buildConstraintViolationWithTemplate("must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters")
                            .addConstraintViolation();
                    break;
                default:
                    break;
            }
        }

        return false;
    }
}
