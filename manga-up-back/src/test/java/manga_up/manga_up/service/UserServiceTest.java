package manga_up.manga_up.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.appUser.AppUserProjection;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.userAdress.UserAddressLittleProjection;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;


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
void shouldFindAllByPage(){
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
void shouldaddFavorite(){

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


}