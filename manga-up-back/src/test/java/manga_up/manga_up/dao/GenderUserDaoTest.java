package manga_up.manga_up.dao;

import manga_up.manga_up.model.GenderUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class GenderUserDaoTest {
    @Autowired
    private GenderUserDao genderUserDao;

    @Test
    void shouldGetAllGenres() {
        //Act
        List<GenderUser> genderUsers = genderUserDao.findAll();

        //Asert
        assertEquals(3, genderUsers.size());
        assertEquals("Homme", genderUsers.get(0).getLabel());
    }
    @Test
    void shouldGetGenreById() {
        GenderUser genderUser = genderUserDao.findById(1).get();

        assertEquals("Homme", genderUser.getLabel());
    }



    @Test
    void shouldSaveGenre() {
        GenderUser genderUser = new GenderUser();
        genderUser.setLabel("Gender Fluid");

        GenderUser savedGenderUser = genderUserDao.save(genderUser);
        assertNotNull(savedGenderUser.getId());
        assertEquals("Gender Fluid", savedGenderUser.getLabel());
    }


    @Test
    void shouldUpdateGenre() {
        GenderUser genderUser = genderUserDao.findById(1).get();
        genderUser.setLabel("New Gender Fluid");

        GenderUser savedGenderUser = genderUserDao.save(genderUser);
        assertEquals("New Gender Fluid", savedGenderUser.getLabel());

    }

    @Test
    void shouldDeleteGenre() {
        genderUserDao.delete(genderUserDao.findById(3).get());

        Optional<GenderUser> genderUser = genderUserDao.findById(3);
        assertFalse(genderUser.isPresent()); }
}