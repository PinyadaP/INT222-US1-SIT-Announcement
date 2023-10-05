package int221.SASBE.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

@Constraint(validatedBy = UniqueNameValidator.class)
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface UniqueName {
    String message() default "does not unique";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}