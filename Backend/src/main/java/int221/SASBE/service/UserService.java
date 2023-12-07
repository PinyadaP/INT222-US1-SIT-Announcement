package int221.SASBE.service;


import int221.SASBE.dto.MatchPasswordDTO;
import int221.SASBE.dto.SimpleUserPostDTO;
import int221.SASBE.dto.SimpleUserPutDTO;
import int221.SASBE.dto.UserPostResDTO;
import int221.SASBE.entities.Announcement;
import int221.SASBE.entities.CustomUserDetails;
import int221.SASBE.entities.User;

import int221.SASBE.repository.AnnouncementRepository;
import int221.SASBE.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnnouncementRepository announcementRepository;
///
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

    private boolean CheckOwner(Announcement announcement, int userId){
        return announcement.getUser().getID() == userId;
    }
///
    public List<User> getUsers() {
        Sort sort = Sort.by(Sort.Direction.ASC, "role","username");
        return userRepository.findAll(sort);
    }
//public List<UserPostResDTO> getUsers() {
//    Sort sort = Sort.by(Sort.Direction.ASC, "role", "username");
//    List<User> users = userRepository.findAll(sort);
//
//    // สร้างรายการผู้ใช้ที่ใช้ UserPostResDTO
//    List<UserPostResDTO> userPostResDTOs = new ArrayList<>();
//    for (User user : users) {
//        UserPostResDTO userPostResDTO = new UserPostResDTO();
//        userPostResDTO.setID(user.getID());
//        userPostResDTO.setUsername(user.getUsername());
//        userPostResDTO.setName(user.getName());
//        userPostResDTO.setEmail(user.getEmail());
//        userPostResDTO.setRole(user.getRole());
//        userPostResDTO.setCreatedOn(user.getCreatedOn());
//        userPostResDTO.setUpdatedOn(user.getUpdatedOn());
//
//        userPostResDTOs.add(userPostResDTO);
//    }
//
//    return userPostResDTOs;
//}


    public User getUserById(Integer ID) {
        return userRepository.findById(ID).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement id" + ID + "does not exist !!!"));
    }

        public User addNewUser(@Valid SimpleUserPostDTO simpleUserPostDTO) {
            User user = new User();
            user.setUsername(simpleUserPostDTO.getUsername().trim());
            user.setName(simpleUserPostDTO.getName().trim());
            user.setEmail(simpleUserPostDTO.getEmail().trim());
            user.setRole(simpleUserPostDTO.getRole().trim());
            user.setPassword(simpleUserPostDTO.getPassword().trim());

            String hashedPassword = hashArgon2(simpleUserPostDTO.getPassword().trim());
            user.setPassword(hashedPassword);

            User newuser = userRepository.saveAndFlush(user);
            userRepository.refresh(newuser);
            return newuser;
    }

    public String hashArgon2(String password ){
        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16,32,1,4096,3);
        return argon2PasswordEncoder.encode(password);
    }


    public ResponseEntity<String> matchPassword(MatchPasswordDTO matchPasswordDTO) {
        User user = userRepository.findByUsername(matchPasswordDTO.getUsername());
        String password = matchPasswordDTO.getPassword();

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified username DOES NOT exist");
        } else {
            Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16, 32, 1, 4096, 3);
            String hashedPassword = user.getPassword();
            boolean passwordMatch = argon2PasswordEncoder.matches(password, hashedPassword);
            if (passwordMatch) {
                return ResponseEntity.ok("Password Matched");
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password NOT Matched");
            }
        }
    }

//    public void removeUser(Integer ID) {
//        User user = userRepository.findById(ID).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User Id " + ID + " DOES NOT EXIST !!!"));
//        userRepository.delete(user);
//    }

    public void removeUser(Integer ID) {
        String currentUserId = getCurrentUserId();
        // Check if the current user is an admin
        if (isAdmin()) {
            int userId = ID;
            User userToUpdate = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id " + userId + " DOES NOT EXIST !!!"));
            User isadmin = userRepository.findById(Integer.valueOf(currentUserId))
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Amin User Not found"));
            // Check if the user to be updated is not the same as the admin user
            if (!currentUserId.equals(String.valueOf(userId))) {
                // find announcementOwner from UserId and then loop for change Owner to Admin
                List<Announcement> announcementsToUpdate = announcementRepository.findByUserId(userToUpdate.getID());
                for (Announcement announcement : announcementsToUpdate) {
                    //set new announcementOwner is admin
                    announcement.setUser(isadmin);
                }
                // Save the updated announcements and delete user
                userRepository.delete(userToUpdate);
                announcementRepository.saveAll(announcementsToUpdate);
            }
            else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
    }






    public User updateUser(@Valid Integer ID, SimpleUserPutDTO simpleUserPutDTO) {
        User existingUser = userRepository.findById(ID).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User Id " + ID + " DOES NOT EXIST !!!"));

        existingUser.setUsername(simpleUserPutDTO.getUsername().trim());
        existingUser.setName(simpleUserPutDTO.getName().trim());
        existingUser.setEmail(simpleUserPutDTO.getEmail().trim());
        existingUser.setRole(simpleUserPutDTO.getRole().trim());


        User newuser = userRepository.saveAndFlush(existingUser);
        userRepository.refresh(newuser);
        return newuser;
    }

}
