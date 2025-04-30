package manga_up.manga_up.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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




public ResponseEntity<?> login( LoginRequestDto user) {
    try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            AppUser appUser = userDao.findByUsername(user.getUsername());

            Map<String, Object> authData = new HashMap<>();
            authData.put("token", jwtUtils.generateToken(appUser.getUsername(), appUser.getRole()));
            authData.put("type", "Bearer");
            return ResponseEntity.ok(authData);
        }
        return ResponseEntity.badRequest().body("Invalid username or password");
    } catch (AuthenticationException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body("Invalid username or password");
    }
}


}
