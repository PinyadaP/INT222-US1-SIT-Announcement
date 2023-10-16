package int221.SASBE.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedHashSet;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "categoryId", nullable = false)
    private Integer categoryId;
    @Column(name = "categoryName", nullable = false, length = 50)
    private String announcementCategory;


}