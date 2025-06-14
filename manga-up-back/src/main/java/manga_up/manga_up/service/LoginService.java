package manga_up.manga_up.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.controller.AuthController;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.login.LoginRequestDto;
import manga_up.manga_up.model.AppUser;

/**
 * Service responsible for user authentication and login handling.
 * 
 * <p>
 * This service authenticates user credentials, generates JWT tokens upon
 * successful login,
 * and sets the JWT token in an HTTP-only cookie in the response.
 * </p>
 */
@Service
public class LoginService {

    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    /**
     * Constructs a new LoginService.
     *
     * @param userDao               the DAO for user data access
     * @param jwtUtils              utility class for JWT token generation and
     *                              validation
     * @param authenticationManager Spring Security authentication manager
     */
    public LoginService(UserDao userDao,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates the user with provided credentials and generates a JWT token if
     * successful.
     * The JWT token is set as an HTTP-only cookie in the HTTP response.
     *
     * @param user     the login request containing username and password
     * @param response the HTTP response to add the JWT cookie
     * @return a {@link ResponseEntity} with a success message and user details if
     *         authentication succeeds,
     *         or a bad request status with error message if authentication fails
     */
    public ResponseEntity<Map<String, Object>> login(LoginRequestDto user, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                AppUser appUser = userDao.findByUsername(user.getUsername());
                String jwt = jwtUtils.generateToken(appUser.getUsername(), appUser.getRole());

                ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(7 * 24 * 60 * 60)
                        .sameSite("None")
                        .build();

                response.setHeader("Set-Cookie", cookie.toString());

                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Login successful");
                responseBody.put("username", appUser.getUsername());
                responseBody.put("role", appUser.getRole());

                return ResponseEntity.ok(responseBody);
            }
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "Invalid username or password");
            return ResponseEntity.badRequest().body(errorBody);
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "Invalid username or password");
            return ResponseEntity.badRequest().body(errorBody);
        }
    }

}
