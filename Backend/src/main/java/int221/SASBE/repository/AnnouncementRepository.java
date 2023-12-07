package int221.SASBE.repository;

import int221.SASBE.entities.Announcement;
import int221.SASBE.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    @Query("SELECT a FROM Announcement a ORDER BY a.announcementId DESC ")
    List<Announcement> findAnnouncement();

    @Query("SELECT a FROM Announcement  a WHERE a.announcementDisplay like 'Y' ")
    List<Announcement> findAnnouncementByDisplayYES(Sort sort);

    @Query("SELECT a FROM Announcement a LEFT JOIN FETCH a.user WHERE a.announcementId = :announcementId")
    Optional<Announcement> findByIdWithUser(@Param("announcementId") Integer announcementId);

    @Query("SELECT a FROM Announcement a WHERE a.user.ID = :userId")
    List<Announcement> findByUserId(@Param("userId") Integer userId);
}
