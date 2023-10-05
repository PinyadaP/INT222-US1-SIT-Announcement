package int221.SASBE.controller;


import int221.SASBE.dto.*;
import int221.SASBE.entities.Announcement;
import int221.SASBE.service.AnnouncementService;
import int221.SASBE.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost", "http://intproj22.sit.kmutt.ac.th"})
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


    @GetMapping("")

    public List<SimpleAnnouncementDTO> getSimpleAnnouncementDTO(@RequestParam(defaultValue = "") String mode) {
        if (!mode.isEmpty()) {
            List<Announcement> announcementList = announcementService.getUserAnnouncement(mode);
            List<SimpleAnnouncementDTO> simpleAnnouncementDTOList = new ArrayList<>(announcementList.size());
            for (Announcement a : announcementList) {
                simpleAnnouncementDTOList.add(modelMapper.map(a, SimpleAnnouncementDTO.class));
            }
            return simpleAnnouncementDTOList;
        }else {
            List<Announcement> announcementList = announcementService.getAnnouncements();
            List<SimpleAnnouncementDTO> simpleAnnouncementDTOList = new ArrayList<>(announcementList.size());
        for (Announcement a : announcementList){
            simpleAnnouncementDTOList.add(modelMapper.map(a, SimpleAnnouncementDTO.class));
        }
            return simpleAnnouncementDTOList;
        }
    }
//    @GetMapping("")
//    public List<SimpleAnnouncementDTO> getSimpleAnnouncementDTO() {
//        List<Announcement> announcementList = announcementService.getAnnouncements();
//        List<SimpleAnnouncementDTO> simpleAnnouncementDTOList = new ArrayList<>(announcementList.size());
//        for (Announcement a : announcementList){
//            simpleAnnouncementDTOList.add(modelMapper.map(a, SimpleAnnouncementDTO.class));
//        }
//            return simpleAnnouncementDTOList;
//    }

    @GetMapping("/{announcementId}")
    public SimpleAnnouncementIdDTO getSimpleAnnouncementById(@PathVariable Integer announcementId) {
        return modelMapper.map(announcementService.getAnnouncementById(announcementId), SimpleAnnouncementIdDTO.class);
    }

    //***ใช้
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public SimpleAnnouncementPostDTO addNewAnnouncement(@RequestBody SimpleAnnouncementResDTO announcementPostResDTO) {
        Announcement announcement = announcementService.addNewAnnouncement(announcementPostResDTO);
        return modelMapper.map(announcement, SimpleAnnouncementPostDTO.class);
    }

    //***ใช้
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{announcementId}")
    public void removeAnnouncement(@PathVariable Integer announcementId) {
        announcementService.removeAnnouncement(announcementId);
    }


    @PutMapping("/{announcementId}")
    public SimpleAnnouncementRqDTO simpleAnnouncementRqDTO(@RequestBody SimpleAnnouncementResDTO announcementResDTO, @PathVariable Integer announcementId) {
        return modelMapper.map(announcementService.updateAnnouncement(announcementId, announcementResDTO), SimpleAnnouncementRqDTO.class);
    }
}








