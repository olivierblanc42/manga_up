package manga_up.manga_up.controller;


import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.UserDao;

import manga_up.manga_up.dto.LoginRequestDto;
import manga_up.manga_up.dto.RegisterDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")

public class AuthController {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    public AuthController(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDTO) {
        if (userDao.findByUsername(registerDTO.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        return ResponseEntity.ok(customUserDetailsService.saveUserDtoRegister(registerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto user) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                Map<String, Object> authDate = new HashMap<>();
                authDate.put("token", jwtUtils.generateToken(user.getUsername()));
                authDate.put("type", "Bearer");
                return ResponseEntity.ok(authDate);
            }
            return ResponseEntity.badRequest().body("Invalid username or password");
        }catch (AuthenticationException e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

}
