package int221.SASBE.dto;

import int221.SASBE.entities.User;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class SimpleAnnouncementDTO {
    @Column(name = "announcementId", nullable = false)
    private Integer id;
    private String announcementTitle;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private String announcementDisplay;
    @Column(name = "categoryName", nullable = false)
    private String announcementCategory;
    private String announcementOwner;
}