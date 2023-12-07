package int221.SASBE.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class SimpleAnnouncementRqDTO {
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private String announcementDisplay;
    private String announcementCategory;
//    @Column(name = "username", nullable = false)
//    private String announcementOwner;
@JsonIgnore
private String userUsername;
    public String getAnnouncementOwner(){
        return this.userUsername;
    }

}
