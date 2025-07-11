package manga_up.manga_up.dao;

import manga_up.manga_up.model.UserAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AddressDaoTest {
    @Autowired
    private AddressDao addressDao;

    @Test
    void shouldGetAllAddresses() {
        List<UserAddress> userAddresses = addressDao.findAll();

        assertEquals(10, userAddresses.size());
        assertEquals("12 rue des Lilas", userAddresses.get(0).getLine1());
    }

    @Test
    void shouldGetAddressById() {
        UserAddress userAddress = addressDao.findById(1).get();
        assertEquals("12 rue des Lilas", userAddress.getLine1());
    }

    @Test
    void shouldSaveAddress() {
        UserAddress userAddress = new UserAddress();
        userAddress.setLine1("31 rue des Coquelicots");
        userAddress.setLine2("Résidence Les Jardins");
        userAddress.setLine3("Appartement 5B");
        userAddress.setCity("Montpellier");
        userAddress.setPostalCode("34000");
        UserAddress savedAddress = addressDao.save(userAddress);
        assertNotNull(savedAddress.getId());
        assertEquals("31 rue des Coquelicots", savedAddress.getLine1());
    }

    @Test
    void shouldUpdateAddress() {
        UserAddress userAddress = addressDao.findById(1).get();
        userAddress.setLine1("354 rue des Coquelicots");
        UserAddress savedAddress = addressDao.save(userAddress);
        assertEquals("354 rue des Coquelicots", savedAddress.getLine1());
    }

    @Test
    void shouldDeleteAddress() {
        addressDao.delete(addressDao.findById(1).get());

        Optional<UserAddress> userAddressOptional = addressDao.findById(1);
        assertFalse(userAddressOptional.isPresent());
    }

}