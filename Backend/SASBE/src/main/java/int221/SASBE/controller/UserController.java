package int221.SASBE.controller;

import int221.SASBE.dto.MatchPasswordDTO;
import int221.SASBE.dto.SimpleUserPostDTO;
import int221.SASBE.dto.SimpleUserPutDTO;
import int221.SASBE.dto.UserPostResDTO;
import int221.SASBE.entities.User;
import int221.SASBE.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost", "http://intproj22.sit.kmutt.ac.th"})

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
@Autowired
private ModelMapper modelMapper;
//    @GetMapping("")
//    @ResponseStatus(HttpStatus.OK)
//    public List<User> getUsers() {
//        return userService.getUsers();
//    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<UserPostResDTO> getUsers() {
        return userService.getUsers();
    }


    @GetMapping("/{ID}")
    public User getUserById(@PathVariable Integer ID) {
        return userService.getUserById(ID);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserPostResDTO addNewUser(@RequestBody @Valid SimpleUserPostDTO simpleUserPostDTO) {
        User user = userService.addNewUser(simpleUserPostDTO);
        return modelMapper.map(user, UserPostResDTO.class);

    }

    @PostMapping("/match")
    @ResponseStatus(HttpStatus.OK)
    public MatchPasswordDTO MatchPassword(@RequestBody MatchPasswordDTO matchPasswordDTO) {
        User user = userService.MatchPassword(matchPasswordDTO);
        return modelMapper.map(user, MatchPasswordDTO.class);

    }

    @DeleteMapping("/{ID}")
    public void removeUser(@PathVariable Integer ID) {
        userService.removeUser(ID);
    }

    @PutMapping("/{ID}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody @Valid SimpleUserPutDTO simpleUserPutDTO, @PathVariable Integer ID) {
        return userService.updateUser(ID, simpleUserPutDTO);
    }

}
