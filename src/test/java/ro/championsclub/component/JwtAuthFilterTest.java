package ro.championsclub.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ro.championsclub.service.JwtService;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private final String jwt = "random_jwt";
    private final String email = "test@email.com";

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternalNoAuthHeaderTest() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, never()).extractUsername(any());
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternalInvalidAuthHeaderTest() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("invalid_header");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, never()).extractUsername(any());
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternalValidTokenAuthenticationSetTest() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(jwt);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtService).isTokenValid(jwt, userDetails);
        verify(filterChain).doFilter(request, response);

        SecurityContext securityContext = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken authToken =
                (UsernamePasswordAuthenticationToken) securityContext.getAuthentication();

        assertThat(userDetails).isEqualTo(authToken.getPrincipal());
        assertThat(userDetails.getAuthorities()).isEqualTo(authToken.getAuthorities());
    }

    @Test
    void doFilterInternalValidTokenInvalidAuthenticationTest() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(jwt);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtService).isTokenValid(jwt, userDetails);
        verify(filterChain).doFilter(request, response);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        assertThat(securityContext.getAuthentication()).isNull();
    }

}
