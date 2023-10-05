package int221.SASBE.service;


import int221.SASBE.dto.MatchPasswordDTO;
import int221.SASBE.dto.SimpleUserPostDTO;
import int221.SASBE.dto.SimpleUserPutDTO;
import int221.SASBE.dto.UserPostResDTO;
import int221.SASBE.entities.User;

import int221.SASBE.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;



//    public List<User> getUsers() {
//        Sort sort = Sort.by(Sort.Direction.ASC, "role","username");
//        return userRepository.findAll(sort);
//    }
public List<UserPostResDTO> getUsers() {
    Sort sort = Sort.by(Sort.Direction.ASC, "role", "username");
    List<User> users = userRepository.findAll(sort);

    // สร้างรายการผู้ใช้ที่ใช้ UserPostResDTO
    List<UserPostResDTO> userPostResDTOs = new ArrayList<>();
    for (User user : users) {
        UserPostResDTO userPostResDTO = new UserPostResDTO();
        userPostResDTO.setID(user.getID());
        userPostResDTO.setUsername(user.getUsername());
        userPostResDTO.setName(user.getName());
        userPostResDTO.setEmail(user.getEmail());
        userPostResDTO.setRole(user.getRole());
        userPostResDTO.setCreatedOn(user.getCreatedOn());
        userPostResDTO.setUpdatedOn(user.getUpdatedOn());

        userPostResDTOs.add(userPostResDTO);
    }

    return userPostResDTOs;
}


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

public User MatchPassword(MatchPasswordDTO matchPasswordDTO){
    User user = userRepository.findByUsername(matchPasswordDTO.getUsername());
    String password = matchPasswordDTO.getPassword();
    String username = matchPasswordDTO.getUsername();


    if (user==null){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The specified username DOES NOT exist");
    } else  {
        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16, 32, 1, 4096, 3);
        String hashedPassword = user.getPassword();
        boolean passwordMatch = argon2PasswordEncoder.matches(password, hashedPassword);

        if (passwordMatch){
             throw  new ResponseStatusException(HttpStatus.OK,"Password Matched");
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Password NOT Matched");
        }

    }

}


    public void removeUser(Integer ID) {
        User user = userRepository.findById(ID).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User Id " + ID + " DOES NOT EXIST !!!"));
        userRepository.delete(user);
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
