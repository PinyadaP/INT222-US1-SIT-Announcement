package int221.SASBE.validator;

import int221.SASBE.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return !userRepository.existsByname(name);
    }
}