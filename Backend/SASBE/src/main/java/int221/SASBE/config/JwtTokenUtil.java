package int221.SASBE.config;

import int221.SASBE.dto.TokenPair;
import int221.SASBE.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        return doGenerateToken(claims, userDetails.getUsername());
//    }
//
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//String token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//        .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessToken()*60*1000))
//        .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey()).compact();
//        return token;
//    }

    public TokenPair generateTokens(UserDetails userDetails) {
        Date now = new Date();
        Date accessTokenExpiration = new Date(now.getTime() + jwtProperties.getAccessToken() * 60 * 1000);
        Date refreshTokenExpiration = new Date(now.getTime() + jwtProperties.getRefreshToken() * 24 * 60 * 60 * 1000);

        String accessToken = doGenerateToken(userDetails.getUsername(), accessTokenExpiration);
        String refreshToken = doGenerateToken(userDetails.getUsername(), refreshTokenExpiration);

        return new TokenPair(accessToken, refreshToken);
    }
    private String doGenerateToken(String subject, Date expiration) {
        Map<String, Object> claims = new HashMap<>();
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


}
