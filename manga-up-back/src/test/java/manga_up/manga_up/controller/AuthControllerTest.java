package manga_up.manga_up.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.configuration.SecurityConfig;
import manga_up.manga_up.controller.AuthController;
import manga_up.manga_up.dto.register.RegisterDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.dto.login.LoginRequestDto;
import manga_up.manga_up.service.AuthorService;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.LoginService;
import manga_up.manga_up.dao.UserDao;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserDao userDao;

  @MockitoBean
  private CustomUserDetailsService customUserDetailsService;
  @MockitoBean
  private PasswordEncoder passwordEncoder;

  @MockitoBean
  private LoginService loginService;

  @MockitoBean
  private JwtUtils jwtUtils;

  @MockitoBean
  private AuthenticationManager authenticationManager;

  @Test
  void shouldRetunrCreateRegisterDto() throws Exception {

    String json = """
        {
          "username": "jean.dupont",
          "firstname": "Jean",
          "lastname": "Dupont",
          "role": "USER",
          "phoneNumber": "0612345678",
          "email": "jean.dupont@example.com",
          "password": "MotDePasseSecurise123!",
          "address": {
            "line1": "123 Rue de l'Exemple",
            "line2": "Appartement 4B",
            "line3": "Bâtiment A",
            "city": "Paris",
            "createdAt": "2025-06-09T07:02:56.254Z",
            "postalCode": "75001"
          },
          "genderUserId": {
            "id": 1,
            "label": "Homme"
          }
        }

        """;

    GenderUserDto gender = new GenderUserDto(1, "Homme");

    UserAddressDto address = new UserAddressDto(
        "123 Rue de l'Exemple",
        "Appartement 4B",
        "Bâtiment A",
        "Paris",
        Instant.parse("2025-06-09T07:02:56.254Z"),
        "75001");

    RegisterDto registerDto = new RegisterDto(
        "jean.dupont",
        "Jean",
        "Dupont",
        "USER",
        "0612345678",
        "jean.dupont@example.com",
        "MotDePasseSecurise123!",
        address,
        gender);

    when(customUserDetailsService.saveUserDtoRegister(any())).thenReturn(registerDto);

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)

    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.username").value("jean.dupont"));
  }

  @Test
  void shouldRetunrCreateRegisterDtoBadRequest() throws Exception {

    String json = """
        {
          "username": "jean.dupont",
          "firstname": "Jean",
          "lastname": "Dupont",
          "role": "USER",
          "phoneNumber": "0612345678",
          "email": "jean.dupont@example.com",
          "password": "MotDePasseSecurise123!",
          "address": {
            "line1": "123 Rue de l'Exemple",
            "line2": "Appartement 4B",
            "line3": "Bâtiment A",
            "city": "Paris",
            "createdAt": "2025-06-09T07:02:56.254Z",
            "postalCode": "75001"
          },
          "genderUserId": {
            "id": 1,
            "label": "Homme"
          }
        }

        """;

    AppUser appUser = new AppUser();
    appUser.setId(1);

    when(userDao.findByUsername("jean.dupont")).thenReturn((appUser));

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Username is already in use"));
  }

  @Test
  void shouldReturnLoginSuccess() throws Exception {
    String json = """
        {
          "username": "jean.dupont",
          "password": "MotDePasseSecurise123!"
        }
        """;

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("message", "Login successful");
    responseBody.put("username", "jean.dupont");
    responseBody.put("role", "USER");

    ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(responseBody);

    when(loginService.login(any(LoginRequestDto.class), any(HttpServletResponse.class)))
        .thenReturn(responseEntity);

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Login successful"))
        .andExpect(jsonPath("$.username").value("jean.dupont"))
        .andExpect(jsonPath("$.role").value("USER"));
  }

  @Test
  @WithMockUser(username = "user", roles = { "ADMIN" })
  void shouldReturnLogoutSuccess() throws Exception {
    mockMvc.perform(post("/api/auth/logout")
        .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Déconnecté avec succès"))
        .andExpect(header().exists("Set-Cookie"))
        .andExpect(header().string("Set-Cookie", Matchers.containsString("Max-Age=0")));
  }

  @Test
  @WithMockUser(username = "user", roles = { "ADMIN" })
  void shouldReturnOkOnCheck() throws Exception {
    mockMvc.perform(get("/api/auth/check")
        .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string(""));
  }

}
