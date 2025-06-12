package manga_up.manga_up.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.filter.JwtFilter;
import manga_up.manga_up.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class JwtFilterTest {

    private JwtFilter jwtFilter;
    private CustomUserDetailsService customUserDetailsService;
    private JwtUtils jwtUtils;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        customUserDetailsService = mock(CustomUserDetailsService.class);
        jwtUtils = mock(JwtUtils.class);
        jwtFilter = new JwtFilter(customUserDetailsService, jwtUtils);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_shouldSkipFiltering_forExcludedUrls() throws Exception {
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_shouldNotAuthenticate_whenNoJwtCookie() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/private");
        when(request.getCookies()).thenReturn(null);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_shouldAuthenticate_whenValidJwtCookie() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/private");
        Cookie jwtCookie = new Cookie("jwt", "valid-token");
        when(request.getCookies()).thenReturn(new Cookie[] { jwtCookie });

        String username = "user1";
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtUtils.extractUsername("valid-token")).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtils.validateToken("valid-token", userDetails)).thenReturn(true);
        when(jwtUtils.extractRoles("valid-token")).thenReturn(List.of("ROLE_USER"));

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(userDetails, auth.getPrincipal());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void doFilter_shouldNotAuthenticate_whenInvalidJwtToken() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/private");
        Cookie jwtCookie = new Cookie("jwt", "invalid-token");
        when(request.getCookies()).thenReturn(new Cookie[] { jwtCookie });

        String username = "user1";
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtUtils.extractUsername("invalid-token")).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtils.validateToken("invalid-token", userDetails)).thenReturn(false);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
