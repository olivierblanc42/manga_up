package manga_up.manga_up.service;

import static org.mockito.Mockito.when;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.appUser.UserProfilDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.dto.manga.MangaLightDto;
import manga_up.manga_up.mapper.AppUserMapper;
import manga_up.manga_up.mapper.MangaMapper;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.appUser.AppUserProjection;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.userAdress.UserAddressLittleProjection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;
    @Mock
    private AppUserMapper appUserMapper;

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
    void shouldFindAllByPage() {
        Pageable pageable = PageRequest.of(0, 5);

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

        when(userDao.FindAllUser(pageable)).thenReturn(page);
        Page<AppUserProjection> result = userService.findAllByPage(pageable);

        assertThat(result).hasSize(2).containsExactly(user1, user2);

    }

    @Test
    void shouldaddFavorite() {

        // Arrange
        Integer userId = 1;
        Integer mangaId = 1;

        userService.addFavorite(userId, mangaId);

        verify(userDao, times(1)).addUserInFavorite(userId, mangaId);

    }

    @Test
    void shouldRemoveFavorite() {

        // Arrange
        Integer userId = 1;
        Integer mangaId = 1;

        userService.removeFavorite(userId, mangaId);

        verify(userDao, times(1)).removeUserInFavorite(userId, mangaId);

    }

    @Test
    void getAuthenticatedUserEntity_shouldReturnUser_whenAuthenticated() throws AccessDeniedException {
        String username = "john";
        AppUser mockUser = new AppUser();
        mockUser.setUsername(username);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userDao.findAppUserByUsername(username)).thenReturn(mockUser);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito
                .mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            AppUser result = userService.getAuthenticatedUserEntity();

            assertNotNull(result);
            assertEquals(username, result.getUsername());

            verify(userDao, times(1)).findAppUserByUsername(username);
        }
    }

    @Test
    void getAuthenticatedUserEntity_shouldThrowAccessDeniedException_whenNotAuthenticated() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        // Pas authentifié
        when(authentication.isAuthenticated()).thenReturn(false);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito
                .mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            assertThrows(AccessDeniedException.class, () -> {
                userService.getAuthenticatedUserEntity();
            });
        }
    }

    @Test
    void getCurrentUser_shouldReturnUserDto_whenUserAuthenticatedAndFound() {
        // Arrange
        String username = "john";
        AppUser mockUser = new AppUser();
        mockUser.setUsername(username);

        UserAddressDto addressDto = new UserAddressDto(
                "12 Rue de la Paix", // line1 (obligatoire, max 50)
                "Appartement 4B", // line2 (optionnel, max 50)
                null, // line3 (optionnel, max 50)
                "Paris", // city (obligatoire, max 100)
                Instant.now(), // createdAt (date de création)
                "75002" // postalCode (obligatoire, max 5)
        );
        addressDto.setLine1("123 Rue Exemple");

        GenderUserDto genderDto = new GenderUserDto(1, "male");

        Set<MangaLightDto> mangas = new HashSet<>();
        // Tu peux ajouter des mangas mocks si tu veux, sinon laisse vide

        UserProfilDto mockDto = new UserProfilDto(
                1,
                "john_doe",
                "John",
                "Doe",
                "USER",
                "0123456789",
                "john.doe@example.com",
                Instant.now(),
                addressDto,
                genderDto,
                mangas);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userDao.findAppUserByUsername(username)).thenReturn(mockUser);
        when(appUserMapper.toDtoAppUser(mockUser)).thenReturn(mockDto);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito
                .mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Act
            ResponseEntity<UserProfilDto> response = userService.getCurrentUser();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(mockDto, response.getBody());
        }
    }


@Test
void isFavorite_shouldReturnTrue_whenCountIsGreaterThanZero() {
    // Arrange
    Integer userId = 1;
    Integer mangaId = 42;

    when(userDao.countFavorite(userId, mangaId)).thenReturn(1);

    // Act
    boolean result = userService.isFavorite(userId, mangaId);

    // Assert
    assertTrue(result);

    verify(userDao, times(1)).countFavorite(userId, mangaId);
}

@Test
void isFavorite_shouldReturnFalse_whenCountIsZero() {
    // Arrange
    Integer userId = 1;
    Integer mangaId = 42;

    when(userDao.countFavorite(userId, mangaId)).thenReturn(0);

    // Act
    boolean result = userService.isFavorite(userId, mangaId);

    // Assert
    assertFalse(result);

    verify(userDao, times(1)).countFavorite(userId, mangaId);
}



}