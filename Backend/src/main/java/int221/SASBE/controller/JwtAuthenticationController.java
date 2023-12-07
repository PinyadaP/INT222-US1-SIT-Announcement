package int221.SASBE.controller;

import int221.SASBE.config.JwtTokenUtil;
import int221.SASBE.dto.DTOToken;
import int221.SASBE.dto.TokenPair;
import int221.SASBE.entities.JwtRequest;
import int221.SASBE.entities.JwtResponse;
import int221.SASBE.entities.User;
import int221.SASBE.repository.UserRepository;
import int221.SASBE.service.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/token")
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping(value = "")
    public ResponseEntity<?> extractRefreshTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String refreshToken = bearerToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(refreshToken));
//         TokenPair token = jwtTokenUtil.generateTokens(userDetails);
        Map<String,String> tokens = jwtTokenUtil.generateTokens(userDetails);

        DTOToken response = new DTOToken();
        response.setToken(tokens.get("access_token"));
//        response.setRefreshToken(token.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        User user = userRepository.findByUsername(authenticationRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username does not exist");
        }
        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16, 32, 1, 4096, 3);
        String hashedPassword = user.getPassword();
        boolean passwordMatch = argon2PasswordEncoder.matches(authenticationRequest.getPassword(), hashedPassword);


        if (!passwordMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
        }
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//            final TokenPair token = jwtTokenUtil.generateTokens(userDetails);
//            JwtResponse response = new JwtResponse();
//            response.setToken(token.getAccessToken());
//            response.setRefreshToken(token.getRefreshToken());
//            return ResponseEntity.status(HttpStatus.OK).body(response);
            Map<String,String> tokens = jwtTokenUtil.generateTokens(userDetails);
            JwtResponse jwtResponse = new JwtResponse(tokens.get("access_token"),tokens.get("refresh_token"));
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


    }
