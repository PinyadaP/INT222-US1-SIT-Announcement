package int221.SASBE.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.ZonedDateTime;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer ID;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "username", nullable = false, length =45,unique=true)
    private String username;
    @Column(name = "name", nullable = false, length = 100,unique=true)
    private String name;
    @Column(name = "email", nullable = false, length = 150,unique=true)
    private String email;
    @Column(name = "role",length = 50)
    private String role;
    @Column(name = "createdOn",insertable = false,updatable = false)
    private ZonedDateTime createdOn;
    @Column(name = "updatedOn",insertable = false,updatable = false)
    private ZonedDateTime updatedOn;


}
