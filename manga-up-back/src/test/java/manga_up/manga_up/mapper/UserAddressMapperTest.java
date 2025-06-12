package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.model.UserAddress;

@ActiveProfiles("test")
public class UserAddressMapperTest {
private UserAddressMapper userAddressMapper;

    @BeforeEach
    void setUp() {
        userAddressMapper = new UserAddressMapper();
    }



     @Test
    void shouldToDto() {
        UserAddress address = new UserAddress();
        address.setId(1);
        address.setLine1("12 rue de l'exemple");
        address.setLine2("Bâtiment A");
        address.setLine3("Appartement 302");
        address.setCity("Paris");
        address.setPostalCode("75001");
        address.setCreatedAt(Instant.now()); 

        UserAddressDto userAddressDto = userAddressMapper.toDto(address);

        assertNotNull(userAddressDto);
        assertEquals("12 rue de l'exemple", userAddressDto.getLine1());
    }



@Test
void shouldToEntity(){
    UserAddressDto dto = new UserAddressDto(
            "15 rue Victor Hugo",
            "Bâtiment B",
            "Escalier 2, 4e étage", 
            "Lyon", 
            Instant.now(), 
            "69003" 
    );
    UserAddress userAddress = userAddressMapper.toEntity(dto);
    assertNotNull(userAddress);
    assertEquals("15 rue Victor Hugo", userAddress.getLine1());
}




}
