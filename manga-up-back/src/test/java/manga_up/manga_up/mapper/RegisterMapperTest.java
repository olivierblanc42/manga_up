package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.dto.register.RegisterDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.GenderUser;
import manga_up.manga_up.model.UserAddress;


@ActiveProfiles("test")
public class RegisterMapperTest {
    private RegisterMapper registerMapper;
    private UserAddressMapper userAddressMapper;
    private GenderUserMapper genderUserMapper;

    @BeforeEach
    void setUp() {
        userAddressMapper = new UserAddressMapper();
        genderUserMapper = new GenderUserMapper();
        registerMapper = new RegisterMapper(userAddressMapper, genderUserMapper);
    }

    @Test
    void shouldtoUserProfilDto() {
        UserAddress address = new UserAddress();
        address.setId(1);
        address.setLine1("12 rue de l'exemple");
        address.setLine2("Bâtiment A");
        address.setLine3("Appartement 302");
        address.setCity("Paris");
        address.setPostalCode("75001");
        address.setCreatedAt(Instant.now());
        GenderUser genderUser = new GenderUser();
        genderUser.setId(1);
        genderUser.setLabel("Homme");


        AppUser appUser = new AppUser();
        appUser.setUsername("John");
        appUser.setFirstname("John");
        appUser.setLastname("Doe");
        appUser.setRole("USER");
        appUser.setPhoneNumber("+3307541232");
        appUser.setEmail("abc@gmail.com");
        appUser.setPassword("password");
        appUser.setIdUserAddress(address);
        appUser.setIdGendersUser(genderUser);

        RegisterDto registerDto = registerMapper.toDtoRegister(appUser);
        assertNotNull(registerDto);
        assertEquals("Doe", registerDto.getLastname());
    }

    @Test
    void shoultoEntityAppUser() {
        UserAddressDto addressDto = new UserAddressDto(
                "12 rue de l'exemple",
                "Bâtiment A",
                "Appartement 302",
                "Paris",
                Instant.now(),
                "42230");
       
        GenderUserDto genderUser = new GenderUserDto(1,"homme");
   

        RegisterDto registerDto = new RegisterDto(
                "john_doe", 
                "John",
                "Doe",
                "USER", 
                "+33612345678", 
                "john.doe@example.com", 
                "password123", 
                addressDto,
                genderUser 
        );

        AppUser appUser = registerMapper.toAppUser(registerDto);
        assertNotNull(appUser);
        assertEquals("Doe", appUser.getLastname());
    }
}
