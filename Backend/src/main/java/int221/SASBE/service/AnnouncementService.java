package int221.SASBE.service;

import int221.SASBE.dto.SimpleAnnouncementResDTO;
import int221.SASBE.entities.Announcement;
import int221.SASBE.entities.Category;
import int221.SASBE.entities.CustomUserDetails;
import int221.SASBE.entities.User;
import int221.SASBE.repository.AnnouncementRepository;
import int221.SASBE.repository.CategoryRepository;
import int221.SASBE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Announcement> getAnnouncements() {
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementId");
        return announcementRepository.findAll(sort);
    }


    public List<Announcement> getAnnouncementbyUserID(int userid){
        List<Announcement> AllAnnouncement = getAnnouncements();
        List<Announcement> filteredByUserID = new ArrayList<>();
        for (Announcement announcement : AllAnnouncement) {
            if (announcement.getUser().getID() == Integer.parseInt(String.valueOf(userid))) {
                filteredByUserID.add(announcement);
            }
        }
        return filteredByUserID;
    }


    public Announcement getAnnouncementById(Integer announcementId) {
        Announcement announcement = announcementRepository.findByIdWithUser(announcementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement id " + announcementId + " does not exist !!!"));

        String currentUserID = getCurrentUserId();
        if (isAnnouncer()){
            if (announcement.getUser().getID() != Integer.valueOf(currentUserID)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this announcement.");
            }

        }
        return announcement;
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        return null;
    }

    private boolean isAnnouncer() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ANNOUNCER"));
    }


    //***ใช้
    public Announcement addNewAnnouncement(SimpleAnnouncementResDTO simpleAnnouncementResDTO) {
        Announcement announcement = new Announcement();
        announcement.setAnnouncementTitle(simpleAnnouncementResDTO.getAnnouncementTitle());
        announcement.setAnnouncementDisplay(simpleAnnouncementResDTO.getAnnouncementDisplay());
        announcement.setAnnouncementDescription(simpleAnnouncementResDTO.getAnnouncementDescription());
        announcement.setPublishDate(simpleAnnouncementResDTO.getPublishDate());
        announcement.setCloseDate(simpleAnnouncementResDTO.getCloseDate());

        Category category = categoryRepository.findById(simpleAnnouncementResDTO.getCategoryId()).orElseThrow(()
                -> new RuntimeException("Category ID: " + simpleAnnouncementResDTO.getCategoryId() + " does not exist"));
        announcement.setCategory(category);
        List<Announcement> announcements = announcementRepository.findAnnouncement();
        Announcement lastAnnouncement = announcements.isEmpty() ? null : announcements.get(0);
        announcement.setAnnouncementId(lastAnnouncement.getAnnouncementId() + 1);

        String userId = getCurrentUserId();
        User user = userRepository.findById(Integer.parseInt(userId)).orElseThrow(()
                -> new RuntimeException("User ID: " + userId + " does not exist"));
        announcement.setUser(user);
        System.out.println("user:"+userId);
        return announcementRepository.saveAndFlush(announcement);
    }
    //***ใช้

    public void removeAnnouncement(Integer announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement id" + announcementId + "does not exist !!!"));
        announcementRepository.delete(announcement);
    }


    public Announcement updateAnnouncement(Integer announcementId, SimpleAnnouncementResDTO announcementResPutDTO) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement ID: " + announcementId + " does not exist"));
        announcement.setAnnouncementTitle(announcementResPutDTO.getAnnouncementTitle());
        announcement.setAnnouncementDescription(announcementResPutDTO.getAnnouncementDescription());
        announcement.setPublishDate(announcementResPutDTO.getPublishDate());
        announcement.setCloseDate(announcementResPutDTO.getCloseDate());
        announcement.setAnnouncementDisplay(announcementResPutDTO.getAnnouncementDisplay());

        Category category = categoryRepository.findById(announcementResPutDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category ID: " + announcementResPutDTO.getCategoryId() + " does not exist"));
        announcement.setCategory(category);

        return announcementRepository.saveAndFlush(announcement);
    }

//-----------------------------FOR USER---------------------------

    public List<Announcement> getUserAnnouncement(String mode) {
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementId");
        List<Announcement> announcement = announcementRepository.findAnnouncementByDisplayYES(sort);
        ZonedDateTime currentDateTime = ZonedDateTime.now();

        if (mode.equals("active")) {
            List<Announcement> filterAnnouncement = new ArrayList<>();
            for (Announcement announcements : announcement) {
                if (announcements.getPublishDate() == null && announcements.getCloseDate() == null) {
                    filterAnnouncement.add(announcements);
                }
                if (announcements.getPublishDate() != null && announcements.getCloseDate() == null) {
                    ZonedDateTime publicDate = announcements.getPublishDate();
                    if (currentDateTime.isAfter(publicDate) || currentDateTime.isEqual(publicDate)) {
                        filterAnnouncement.add(announcements);
                    }
                }
                if (announcements.getPublishDate() != null && announcements.getCloseDate() != null) {
                    ZonedDateTime publicDate = announcements.getPublishDate();
                    ZonedDateTime closeDate = announcements.getCloseDate();
                    if (currentDateTime.isAfter(publicDate) || currentDateTime.isEqual(publicDate)) {
                        if (currentDateTime.isBefore(closeDate)) {
                            filterAnnouncement.add(announcements);
                        }
                    }
                }
                if (announcements.getPublishDate() == null && announcements.getCloseDate() != null) {
                    ZonedDateTime closeDate = announcements.getCloseDate();
                    if (currentDateTime.isBefore(closeDate)) {
                        filterAnnouncement.add(announcements);
                    }
                }
            }
            return filterAnnouncement;
        }
        if (mode.equals("close")) {
            List<Announcement> filter = new ArrayList<>();
            for (Announcement announcements : announcement) {
                if (announcements.getCloseDate() != null) {
                    ZonedDateTime closeDate = announcements.getCloseDate();
                    if (currentDateTime.isAfter(closeDate) || currentDateTime.isEqual(closeDate)) {
                        filter.add(announcements);
                    }
                }
            }
            return filter;
        } else {
            throw new RuntimeException("Mode NOT_FOUND");
        }
    }

}


