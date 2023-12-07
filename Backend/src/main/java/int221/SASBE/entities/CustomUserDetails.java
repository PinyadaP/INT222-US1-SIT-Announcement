package int221.SASBE.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private User alreadyUser;
    private String userId;
//    private Set<CustomGrantedAuthority> authorities;



    public CustomUserDetails(User alreadyUser, String userId) {
        this.alreadyUser = alreadyUser;
        this.userId = userId;

    }


    // Implement UserDetails interface methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = alreadyUser.getRole().toUpperCase();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role);
        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return alreadyUser.getPassword();
    }

    @Override
    public String getUsername() {
        return alreadyUser.getUsername();
    }


    public String getUserId() {
        return userId;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement custom logic here
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can implement custom logic here
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can implement custom logic here
    }

    @Override
    public boolean isEnabled() {
        return true; // You can implement custom logic here
    }
}
