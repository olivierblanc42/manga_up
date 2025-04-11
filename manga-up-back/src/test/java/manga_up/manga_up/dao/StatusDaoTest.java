package manga_up.manga_up.dao;

import manga_up.manga_up.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StatusDaoTest {
 @Autowired
 private StatusDao statusDao;

    @Test
    void shouldFindAll() {
        List<Status> status = statusDao.findAll();

        assertEquals(3, status.size());
        assertEquals("En cours", status.get(0).getLabel());
    }

    @Test
    void shouldFindById() {
        Status status = statusDao.findById(1).get();
        assertEquals("En cours", status.getLabel());
    }

    @Test
    void shouldSaveStatus() {
        Status status = new Status();
        status.setLabel("Archiver");
      Status savedStatus = statusDao.save(status);
      assertNotNull(savedStatus.getId());
      assertEquals("Archiver", savedStatus.getLabel());

    }


    @Test
    void shouldUpdateStatus() {
        Status status = statusDao.findById(1).get();

        status.setLabel("Archiver");
        Status savedStatus = statusDao.save(status);
        assertEquals("Archiver", savedStatus.getLabel());
    }

    @Test
    void shouldDeleteStatus() {
        statusDao.delete(statusDao.findById(1).get());
        Optional<Status> status = statusDao.findById(1);
        assertFalse(status.isPresent());
    }

}