package au.com.auspost.security;

import au.com.auspost.domain.User;
import au.com.auspost.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;

@Component
public class EarthquakeUserDetailsService implements UserDetailsService {
    @Inject
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User wnout found - %s", username));
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), new ArrayList<GrantedAuthority>());
        return userDetails;
    }
}
