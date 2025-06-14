package manga_up.manga_up.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.test.web.servlet.MockMvc;

import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.appUser.UserProfilDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.dto.manga.MangaLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.projection.appUser.AppUserProjection;
import manga_up.manga_up.projection.userAdress.UserAddressLittleProjection;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.UserService;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    private static class TestUserProjection implements AppUserProjection {
        private final Integer id;
        private final String username;
        private final String firstname;
        private final String lastname;
        private final String email;
        private final String phoneNumber;
        private final UserAddressLittleProjection idUserAddress;

        public TestUserProjection(Integer id, String username, String firstname, String lastname,
                String email, String phoneNumber, UserAddressLittleProjection idUserAddress) {
            this.id = id;
            this.username = username;
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.idUserAddress = idUserAddress;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getFirstname() {
            return firstname;
        }

        @Override
        public String getLastname() {
            return lastname;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getPhoneNumber() {
            return phoneNumber;
        }

        @Override
        public UserAddressLittleProjection getIdUserAddress() {
            return idUserAddress;
        }
    }

    public class TestUserAddressLittleProjection implements UserAddressLittleProjection {
        private final String line1;
        private final String line2;
        private final String line3;
        private final String city;
        private final String postalCode;

        public TestUserAddressLittleProjection(String line1, String line2, String line3, String city,
                String postalCode) {
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
            this.city = city;
            this.postalCode = postalCode;
        }

        @Override
        public String getLine1() {
            return line1;
        }

        @Override
        public String getLine2() {
            return line2;
        }

        @Override
        public String getLine3() {
            return line3;
        }

        @Override
        public String getCity() {
            return city;
        }

        @Override
        public String getPostalCode() {
            return postalCode;
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnPaginatedUsers() throws Exception {
        TestUserAddressLittleProjection address1 = new TestUserAddressLittleProjection(
                "123 Rue Principale", "Bâtiment A", null, "Paris", "75001");

        TestUserAddressLittleProjection address2 = new TestUserAddressLittleProjection(
                "456 Avenue Secondaire", null, null, "Lyon", "69002");

        TestUserProjection user1 = new TestUserProjection(
                1, "user1", "John", "Doe", "john.doe@example.com", "1234567890", address1);
        ;
        TestUserProjection user2 = new TestUserProjection(
                2, "user2", "Jane", "Smith", "jane.smith@example.com", "0987654321", address2);

        Page<AppUserProjection> page = new PageImpl<>(List.of(user1, user2));
        when(userService.findAllByPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].username").value("user1"))
                .andExpect(jsonPath("$.content[1].username").value("user2"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnCurrentUserProfile() throws Exception {

        UserAddressDto address = new UserAddressDto(
                "123 Rue Principale",
                "Appartement 5B",
                "",
                "Paris",
                Instant.now(),
                "75001");
        GenderUserDto gender = new GenderUserDto(1, "Male");
        Set<MangaLightDto> mangas = new HashSet<>();
        mangas.add(new MangaLightDto(1, "Naruto"));
        mangas.add(new MangaLightDto(2, "One Piece"));
        UserProfilDto userProfile = new UserProfilDto(
                1,
                "johndoe",
                "John",
                "Doe",
                "USER",
                "0123456789",
                "john.doe@example.com",
                Instant.now(),
                address,
                gender,
                mangas);

        when(userService.getCurrentUser())
                .thenReturn(ResponseEntity.ok(userProfile));

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("0123456789"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnIsFavoriteTrue() throws Exception {
        AppUser mockUser = new AppUser();
        mockUser.setId(1);

        int mangaId = 42;

        when(userService.getAuthenticatedUserEntity()).thenReturn(mockUser);
        when(userService.isFavorite(1, mangaId)).thenReturn(true);

        mockMvc.perform(get("/api/users/manga/{mangaId}/is-favorite", mangaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }


    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnIsFavoriteNonAuthenticate() throws Exception {
        int mangaId = 42;

        when(userService.getAuthenticatedUserEntity()).thenThrow(new RuntimeException("Utilisateur non authentifié"));

        mockMvc.perform(get("/api/users/manga/{mangaId}/is-favorite", mangaId))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Utilisateur non authentifié"));
    }




    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnRemoveFavorite() throws Exception {
        AppUser mockUser = new AppUser();
        mockUser.setId(1);

        int mangaId = 42;

        when(userService.getAuthenticatedUserEntity()).thenReturn(mockUser);
        doNothing().when(userService).removeFavorite(anyInt(), anyInt());

        mockMvc.perform(delete("/api/users/manga/{mangaId}", mangaId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Manga supprimé des favoris avec succès."));

    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnAddFavorite() throws Exception {
        AppUser mockUser = new AppUser();
        mockUser.setId(1);

        int mangaId = 42;

        when(userService.getAuthenticatedUserEntity()).thenReturn(mockUser);
        doNothing().when(userService).addFavorite(anyInt(), anyInt());

        mockMvc.perform(post("/api/users/manga/{mangaId}", mangaId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Manga ajouté aux favoris avec succès."));

    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnForbiddenWhenAddFavoriteThrows() throws Exception {
        AppUser mockUser = new AppUser();
        mockUser.setId(1);

        int mangaId = 42;

        when(userService.getAuthenticatedUserEntity()).thenReturn(mockUser);
        doThrow(new RuntimeException("Erreur")).when(userService).removeFavorite(anyInt(), anyInt());

        mockMvc.perform(delete("/api/users/manga/{mangaId}", mangaId).with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Erreur lors de la suppression des favoris."));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnForbiddenWhenDeleteavoriteThrows() throws Exception {
        AppUser mockUser = new AppUser();
        mockUser.setId(1);

        int mangaId = 42;

        when(userService.getAuthenticatedUserEntity()).thenReturn(mockUser);
        doThrow(new RuntimeException("Erreur")).when(userService).addFavorite(anyInt(), anyInt());

        mockMvc.perform(post("/api/users/manga/{mangaId}", mangaId).with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Erreur lors de l'ajout aux favoris."));
    }






    @Test
    void shouldReturnForbiddenWhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }
}
