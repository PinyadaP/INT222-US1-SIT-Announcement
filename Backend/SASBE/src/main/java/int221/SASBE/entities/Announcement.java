package int221.SASBE.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@Entity
@Table(name = "announces")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcementId", nullable = false)
    private Integer announcementId;
    @Column(name = "announcementTitle", nullable = false, length = 200)
    private String announcementTitle;
    @Column(name = "announcementDescription", nullable = false, length = 10000)
    private String announcementDescription;
    @Column(name = "publishDate")
    private ZonedDateTime publishDate;
    @Column(name = "closeDate")
    private ZonedDateTime closeDate;
    @Column(name = "announcementDisplay", nullable = false)
    private String announcementDisplay;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categories_categoryId")
    private Category category;

}