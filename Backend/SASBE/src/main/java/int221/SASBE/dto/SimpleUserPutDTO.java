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
    @UniqueUsername
    @NotBlank(message = "must not be blank")
    @Size(min = 1,max = 45, message = "size must be between 1 and 45")
    private String username;
    @UniqueName
    @NotBlank(message = "must not be blank")
    @Size(min = 1,max = 100, message = "size must be between 1 and 100")
    private String name;
    @Size(min = 1,max = 150, message = "size must be between 1 and 150")
    @NotBlank
    @UniqueEmail
    @Email
    private String email;
    private String role;
    @Column(name = "createdOn",insertable = false,updatable = false)
    private ZonedDateTime createdOn;
    @Column(name = "updatedOn",insertable = false,updatable = false)
    private ZonedDateTime updatedOn;
}
