package manga_up.manga_up.dao;

import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.model.GenderUser;
import manga_up.manga_up.model.UserAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private GenderUserDao genderUserDao;
    @Autowired
    private AddressDao addressDao;

    @Test
    void shouldGetAllUser() {
        //Act
        List<AppUser> appUsers = userDao.findAll();

        //Asert
        assertEquals(2, appUsers.size());
        assertEquals("Doe", appUsers.get(0).getLastname());
    }

    @Test
    void shouldGetUserById() {
        AppUser appUser = userDao.findById(1).get();

        assertEquals("johndoe", appUser.getUsername());
    }

    @Test
    void shouldSaveUser() {
        GenderUser genderUser = new GenderUser();
        genderUser.setLabel("Gender Fluid");
        GenderUser saveGenderUser = genderUserDao.save(genderUser);

        UserAddress userAddress = new UserAddress();
        userAddress.setLine1("31 rue des Coquelicots");
        userAddress.setLine2("Résidence Les Jardins");
        userAddress.setLine3("Appartement 5B");
        userAddress.setCity("Montpellier");
        userAddress.setPostalCode("34000");
        UserAddress savedAddress = addressDao.save(userAddress);
        AppUser appUser = new AppUser();
        appUser.setUsername("johndoe2");
        appUser.setFirstname("John");
        appUser.setLastname("Doe2");
        appUser.setRole("ROLE_USER");
        appUser.setPhoneNumber("0123456789");
        appUser.setPassword("123456");
        appUser.setEmail("johndoe2@gmail.com");
        appUser.setCreatedAt(Instant.parse("2023-04-10T10:00:00Z"));
        appUser.setIdGendersUser(saveGenderUser);
        appUser.setIdUserAddress(savedAddress);
       AppUser savedAppUser = userDao.save(appUser);
       assertNotNull(savedAppUser.getId());
       assertEquals("johndoe2", savedAppUser.getUsername());
    }


    @Test
    void shouldDeleteUser() {
        userDao.delete(userDao.findById(1).get());
        Optional<AppUser> appUser = userDao.findById(1);
        assertFalse(appUser.isPresent());

    }

}