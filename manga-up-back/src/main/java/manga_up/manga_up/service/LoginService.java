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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletResponse;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.controller.AuthController;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.login.LoginRequestDto;
import manga_up.manga_up.model.AppUser;

@Service
public class LoginService {
    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    public LoginService(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        this.userDao = userDao;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        
    }




public ResponseEntity<?> login(LoginRequestDto user, HttpServletResponse response) {
    try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            AppUser appUser = userDao.findByUsername(user.getUsername());
            String jwt = jwtUtils.generateToken(appUser.getUsername(), appUser.getRole());

            ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .secure(false) // ✅ passe à true en prod
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) 
                    .sameSite("Strict") // ou "Lax"
                    .build();

            response.setHeader("Set-Cookie", cookie.toString());
            
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("username", appUser.getUsername());
            responseBody.put("role", appUser.getRole());

            return ResponseEntity.ok(responseBody);
        }
        return ResponseEntity.badRequest().body("Invalid username or password");
    } catch (AuthenticationException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body("Invalid username or password");
    }
}



}
