package au.com.auspost.unit.security;

import au.com.auspost.domain.User;
import au.com.auspost.repository.UserRepository;
import au.com.auspost.security.EarthquakeUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class EarthquakeUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private User user;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testLoadUserByUsernameIsSuccessful()
    {
        EarthquakeUserDetailsService target = new EarthquakeUserDetailsService();
        ReflectionTestUtils.setField(target, "userRepository",
                userRepository);

        //Mock calls to the user repository and the user
        when(userRepository.findByUsername("fred")).thenReturn(user);
        when(user.getUsername()).thenReturn("Fred");
        when(user.getPassword()).thenReturn("password");

        UserDetails userDetails = target.loadUserByUsername("fred");
        assertNotNull(userDetails);
        verify(userRepository, times(1)).findByUsername("fred");
        verify(user, atLeastOnce()).getUsername();
        verify(user, atLeastOnce()).getPassword();
    }
    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameThrowsException()
    {
        EarthquakeUserDetailsService target = new EarthquakeUserDetailsService();
        ReflectionTestUtils.setField(target, "userRepository",
                userRepository);

        //Mock calls to the user repository and assume user is not returned
        when(userRepository.findByUsername("fred")).thenReturn(null);
        target.loadUserByUsername("fred");
    }
}
