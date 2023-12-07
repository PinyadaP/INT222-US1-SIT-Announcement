package int221.SASBE.controller;


import int221.SASBE.dto.*;
import int221.SASBE.entities.Announcement;
import int221.SASBE.entities.CustomUserDetails;
import int221.SASBE.service.AnnouncementService;
import int221.SASBE.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = {"http://localhost", "http://intproj22.sit.kmutt.ac.th"})
//@CrossOrigin(origins = "http://localhost")
//@CrossOrigin(origins = "http://ip22us1.sit.kmutt.ac.th")
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private CategoryService categoryService;


    @Autowired
    private ModelMapper modelMapper;

    ///////////////
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        return null;
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isAnnouncer() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ANNOUNCER"));
    }

    ////////////////
    @GetMapping("")
    public List<SimpleAnnouncementDTO> getSimpleAnnouncementDTO(@RequestParam(defaultValue = "") String mode) {

        if (isAnnouncer()){
            String CurrentUserId = getCurrentUserId();
            List<Announcement> announcements = announcementService.getAnnouncementbyUserID(Integer.parseInt(CurrentUserId));
            List<SimpleAnnouncementDTO> simpleAnnouncementDTOList = new ArrayList<>(announcements.size());
            for (Announcement a : announcements) {
                SimpleAnnouncementDTO simpleAnnouncementDTO = modelMapper.map(a, SimpleAnnouncementDTO.class);
                // Access the User entity to get the username
                String announcementOwner = a.getUser() != null ? a.getUser().getUsername() : null;
                simpleAnnouncementDTO.setAnnouncementOwner(announcementOwner);
                simpleAnnouncementDTOList.add(simpleAnnouncementDTO);
            }
            return simpleAnnouncementDTOList;
        }else {
            List<Announcement> announcementList;
            if (!mode.isEmpty()) {
                announcementList = announcementService.getUserAnnouncement(mode);
            } else {
                announcementList = announcementService.getAnnouncements();
            }
            List<SimpleAnnouncementDTO> simpleAnnouncementDTOList = new ArrayList<>(announcementList.size());
        for (Announcement a : announcementList) {
            SimpleAnnouncementDTO simpleAnnouncementDTO = modelMapper.map(a, SimpleAnnouncementDTO.class);
            // Access the User entity to get the username
            String announcementOwner = a.getUser() != null ? a.getUser().getUsername() : null;
            simpleAnnouncementDTO.setAnnouncementOwner(announcementOwner);
            simpleAnnouncementDTOList.add(simpleAnnouncementDTO);
        }
        return simpleAnnouncementDTOList;
        }

//        List<SimpleAnnouncementDTO> simpleAnnouncementDTOList = new ArrayList<>(announcementList.size());
//        for (Announcement a : announcementList) {
//            SimpleAnnouncementDTO simpleAnnouncementDTO = modelMapper.map(a, SimpleAnnouncementDTO.class);
//            // Access the User entity to get the username
//            String announcementOwner = a.getUser() != null ? a.getUser().getUsername() : null;
//            simpleAnnouncementDTO.setAnnouncementOwner(announcementOwner);
//            simpleAnnouncementDTOList.add(simpleAnnouncementDTO);
//        }
//        return simpleAnnouncementDTOList;
    }

//    @GetMapping("/{announcementId}")
//    public SimpleAnnouncementIdDTO getSimpleAnnouncementById(@PathVariable Integer announcementId) {
//        return modelMapper.map(announcementService.getAnnouncementById(announcementId), SimpleAnnouncementIdDTO.class);
//    }

//    @GetMapping("/{announcementId}")
//    public SimpleAnnouncementIdDTO getSimpleAnnouncementById(@PathVariable Integer announcementId) {
//        Announcement announcement = announcementService.getAnnouncementById(announcementId);
//        SimpleAnnouncementIdDTO dto = modelMapper.map(announcement, SimpleAnnouncementIdDTO.class);
//        dto.setAnnouncementOwner(announcement.getUser().getUsername()); // Manually set the announcementOwner
//        return dto;
//    }

    @GetMapping("/{announcementId}")
    public SimpleAnnouncementIdDTO getSimpleAnnouncementById(@PathVariable Integer announcementId) {
        Announcement announcement = announcementService.getAnnouncementById(announcementId);
            SimpleAnnouncementIdDTO dto = modelMapper.map(announcement, SimpleAnnouncementIdDTO.class);
            dto.setAnnouncementOwner(announcement.getUser().getUsername());
            return dto;

    }



    //    private boolean CreateOwner(Announcement announcement){
//        return announcement;
//    }
    //***ใช้
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public SimpleAnnouncementPostDTO addNewAnnouncement(@RequestBody SimpleAnnouncementResDTO announcementPostResDTO) {
        Announcement announcement = announcementService.addNewAnnouncement(announcementPostResDTO);
        if (isAdmin() || (isAnnouncer())){
            return modelMapper.map(announcement, SimpleAnnouncementPostDTO.class);
        }else if (isAnnouncer()){
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN,"access denine");
        }else throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,"not acess");
    }

    //***ใช้
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{announcementId}")
    public void removeAnnouncement(@PathVariable Integer announcementId) {
        String userId = getCurrentUserId();
        Announcement announcement = announcementService.getAnnouncementById(announcementId);
        if (isAdmin() || (isAnnouncer()&&CheckOwner(announcement, Integer.parseInt(userId)))){
            announcementService.removeAnnouncement(announcementId);
        }else if (isAnnouncer()){
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN,"access denine");
        }else throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,"not acess");
    }




    private boolean CheckOwner(Announcement announcement,int userId){
        return announcement.getUser().getID() == userId;
    }

    @PutMapping("/{announcementId}")
    public SimpleAnnouncementRqDTO simpleAnnouncementRqDTO(@RequestBody SimpleAnnouncementResDTO announcementResDTO, @PathVariable Integer announcementId) {
        String userId = getCurrentUserId();
        Announcement announcement = announcementService.getAnnouncementById(announcementId);
        if (isAdmin() || (isAnnouncer()&&CheckOwner(announcement, Integer.parseInt(userId)))){
            return modelMapper.map(announcementService.updateAnnouncement(announcementId, announcementResDTO), SimpleAnnouncementRqDTO.class);
        }else if (isAnnouncer()){
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN,"access denine");
        }else throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,"not acess");
    }
}








