package manga_up.manga_up.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import jakarta.servlet.http.HttpServletResponse;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.login.LoginRequestDto;
import manga_up.manga_up.model.AppUser;

@ActiveProfiles("test")

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

  @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDao userDao;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private LoginService loginService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private Authentication authentication;

    @Test
    void shoutestLogin() {

    }


   @Test
    void login_shouldReturnSuccessResponse_whenCredentialsAreValid() {
        // Given
        String username = "john";
        String password = "password";
        String role = "USER";
        String token = "mocked.jwt.token";

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        AppUser mockUser = new AppUser();
        mockUser.setUsername(username);
        mockUser.setRole(role);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userDao.findByUsername(username)).thenReturn(mockUser);
        when(jwtUtils.generateToken(username, role)).thenReturn(token);

        ArgumentCaptor<String> cookieCaptor = ArgumentCaptor.forClass(String.class);

        // When
        ResponseEntity<Map<String, Object>> response = loginService.login(loginRequest, httpServletResponse);

        // Then
        assertNotNull(response.getBody());
        Map<String, Object> body = response.getBody(); // tu peux faire ça si tu préfères
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", body.get("message"));
        assertEquals(username, body.get("username"));
        assertEquals(role, body.get("role"));

        verify(httpServletResponse).setHeader(eq("Set-Cookie"), cookieCaptor.capture());
        String cookieValue = cookieCaptor.getValue();
        assertTrue(cookieValue.contains("jwt=" + token));
        assertTrue(cookieValue.contains("HttpOnly"));
    }

@Test
void login_shouldReturnBadRequest_whenAuthenticationFails() {
    // Données d’entrée
    LoginRequestDto loginRequest = new LoginRequestDto();
    loginRequest.setUsername("wrong");
    loginRequest.setPassword("bad");

    // Mock : simulate auth exception
    when(authenticationManager.authenticate(any(Authentication.class)))
            .thenThrow(new BadCredentialsException("Bad credentials"));

    // Appel
    ResponseEntity<Map<String, Object>> response = loginService.login(loginRequest, httpServletResponse);

    // Vérifications
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid username or password", response.getBody().get("error"));
}


}
