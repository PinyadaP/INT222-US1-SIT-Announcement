package int221.SASBE.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class SimpleAnnouncementPostDTO {
    @Column(name = "announcementId", nullable = false)
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private String announcementDisplay;
    private String announcementCategory;
    @JsonIgnore
    private String userUsername;
    public String getAnnouncementOwner(){
        return this.userUsername;
    }
}
