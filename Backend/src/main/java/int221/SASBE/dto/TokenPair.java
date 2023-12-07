package int221.SASBE.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TokenPair {
    private String accessToken;
    private String refreshToken;

    public TokenPair(String accessToken,String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


}
