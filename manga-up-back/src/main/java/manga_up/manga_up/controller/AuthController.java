package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.login.LoginRequestDto;
import manga_up.manga_up.dto.register.RegisterDto;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.LoginService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "1.Auth", description = "Operations related to authentication")
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final LoginService loginService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
            AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService,
            LoginService loginService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDTO) {
        if (userDao.findByUsername(registerDTO.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        RegisterDto resultDto = customUserDetailsService.saveUserDtoRegister(registerDTO);

        return ResponseEntity.ok(resultDto);
    }

 @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequestDto user, HttpServletResponse response) {
    return loginService.login(user, response);
}

@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletResponse response) {
    ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(true) 
            .path("/")
            .sameSite("None")
            .maxAge(0) 
            .build();

    response.addHeader("Set-Cookie", deleteCookie.toString());


    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("message", "Déconnecté avec succès");

    return ResponseEntity.ok(responseBody);
}

@GetMapping("/check")
public ResponseEntity<?> checkAuth(){
    return ResponseEntity.ok().build();
}


}
