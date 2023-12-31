package int221.SASBE.config;

import int221.SASBE.dto.TokenPair;
import int221.SASBE.entities.CustomUserDetails;
import int221.SASBE.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {
    @Autowired
    private JwtProperties jwtProperties;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public Map<String,String> generateTokens(UserDetails userDetails) {
        Date now = new Date();
        Map<String,Object> claims = new HashMap<>();
        Date accessTokenExpiration = new Date(now.getTime() + jwtProperties.getAccessToken() * 60 * 1000);
        Date refreshTokenExpiration = new Date(now.getTime() + jwtProperties.getRefreshToken() * 60 * 1000);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String userId = null;
        if (userDetails instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) userDetails).getUserId();
            claims.put("userId",userId);
        }
        claims.put("roles",roles);

//        String accessToken = doGenerateToken(claims,userDetails.getUsername(), accessTokenExpiration);
//        String refreshToken = doGenerateToken(claims,userDetails.getUsername(), refreshTokenExpiration);


       claims.put("access_token",doGenerateToken(claims,userDetails.getUsername(), accessTokenExpiration));
       claims.put("refresh_token",doGenerateToken(claims,userDetails.getUsername(), refreshTokenExpiration));
       Map<String,String> tokens = new HashMap<>();
       tokens.put("access_token", (String) claims.get("access_token"));
       tokens.put("refresh_token", (String) claims.get("refresh_token"));
       return tokens;
    }
    private String doGenerateToken(Map<String,Object> claims,String subject, Date expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
                .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /////////////

}
