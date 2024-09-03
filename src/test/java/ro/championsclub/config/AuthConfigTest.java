package ro.championsclub.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.championsclub.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private AuthConfig authConfig;

    @Test
    void userDetailsService_shouldReturnUserDetailsService() {
        UserDetailsService userDetailsService = authConfig.userDetailsService();

        assertThat(userDetailsService).isNotNull();
    }

    @Test
    void authenticationManager_shouldReturnAuthenticationManager() throws Exception {
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthManager);

        AuthenticationManager authenticationManager = authConfig.authenticationManager(authenticationConfiguration);

        assertThat(authenticationManager).isNotNull();
        verify(authenticationConfiguration).getAuthenticationManager();
    }

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        PasswordEncoder passwordEncoder = authConfig.passwordEncoder();

        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.class);
    }

}
