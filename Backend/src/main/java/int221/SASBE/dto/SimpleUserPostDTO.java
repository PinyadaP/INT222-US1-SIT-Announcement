package int221.SASBE.dto;

import int221.SASBE.validator.UniqueEmail;
import int221.SASBE.validator.UniqueName;
import int221.SASBE.validator.UniqueUsername;
import int221.SASBE.validator.ValidPassword;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
@Getter
@Setter
public class SimpleUserPostDTO {
    @NotBlank
    @UniqueUsername
    @Size(min = 1,max = 45, message = "size must be between 1 and 45")
    private String username;
    @NotBlank(message = "must not be blank")
    @ValidPassword
    @Size(min = 1,max = 100, message = "{size must be between 8 and 14}")
    private String password;
    @NotBlank(message = "must not be blank")
    @Size(min = 1,max = 100, message = "size must be between 1 and 100")
    @UniqueName
    private String name;
    @UniqueEmail
    @Size(min = 1,max = 150, message = "size must be between 1 and 150")
    @Email(message = "Email should be valid")
    @NotBlank
    private String email;
    private String role;

}
