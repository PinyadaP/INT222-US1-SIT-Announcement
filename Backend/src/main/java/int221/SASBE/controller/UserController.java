package int221.SASBE.controller;

import int221.SASBE.config.JwtTokenUtil;
import int221.SASBE.dto.*;
import int221.SASBE.entities.Announcement;
import int221.SASBE.entities.CustomUserDetails;
import int221.SASBE.entities.User;
import int221.SASBE.repository.AnnouncementRepository;
import int221.SASBE.service.AnnouncementService;
import int221.SASBE.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin(origins = {"http://localhost", "http://intproj22.sit.kmutt.ac.th"})
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AnnouncementService announcementService;

    ///
    private boolean isAdmin(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean CheckOwner(Announcement announcement, int userId){
        return announcement.getUser().getID() == userId;
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
///

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<UserPostResDTO> getUsers() {
        if (isAdmin()){
            List<User> users = userService.getUsers();
            return users.stream()
                    .map(user -> modelMapper.map(user, UserPostResDTO.class))
                    .collect(Collectors.toList());
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{ID}")
    public SimpleUserPutDTO getUserById(@PathVariable Integer ID) {
        if (isAdmin()){
        return modelMapper.map(userService.getUserById(ID), SimpleUserPutDTO.class);
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }



//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserPostResDTO addNewUser(@RequestBody @Valid SimpleUserPostDTO simpleUserPostDTO) {
        if (isAdmin()){
        User user = userService.addNewUser(simpleUserPostDTO);
        return modelMapper.map(user, UserPostResDTO.class);
        }
        else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
    }

//    @PreAuthorize("hasRole('ADMIN')")
@PostMapping("/match")
public ResponseEntity<String> matchPassword(@RequestBody MatchPasswordDTO matchPasswordDTO) {
//    ResponseEntity<String> user = userService.matchPassword(matchPasswordDTO);
//
//    if (user != null) {
//        return ResponseEntity.status(HttpStatus.OK).body("Password Matched");
//    } else {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password NOT Matched");
//    }
    ResponseEntity<String> user = userService.matchPassword(matchPasswordDTO);

    if (isAdmin()) {
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Password Matched");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password NOT Matched");
        }
    } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access forbidden for non-admin users");
    }
}




//    @PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{ID}")
public void removeUser(@PathVariable Integer ID) throws AccessDeniedException {
    if (isAdmin()){
        userService.removeUser(ID);
    }
    else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}



//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{ID}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody @Valid SimpleUserPutDTO simpleUserPutDTO, @PathVariable Integer ID) {
        if (isAdmin()) {
            return userService.updateUser(ID, simpleUserPutDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
