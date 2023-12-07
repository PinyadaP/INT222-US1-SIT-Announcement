package int221.SASBE.dto;

import int221.SASBE.validator.UniqueEmail;
import int221.SASBE.validator.UniqueName;
import int221.SASBE.validator.UniqueUsername;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
@Getter
@Setter
public class SimpleUserPutDTO {

    private Integer ID;
    private String username;
    private String name;
    private String email;
    private String role;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
