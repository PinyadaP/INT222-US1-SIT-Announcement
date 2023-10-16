package int221.SASBE.repository;

import int221.SASBE.entities.Announcement;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    @Query("SELECT a FROM Announcement a ORDER BY a.announcementId DESC ")
    List<Announcement> findAnnouncement();

    @Query("SELECT a FROM Announcement  a WHERE a.announcementDisplay like 'Y' ")
    List<Announcement> findAnnouncementByDisplayYES(Sort sort);

}
