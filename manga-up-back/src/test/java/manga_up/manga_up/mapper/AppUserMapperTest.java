package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.UserAdress.UserAdressDtoUpdate;
import manga_up.manga_up.dto.appUser.UpdateUserDto;
import manga_up.manga_up.dto.appUser.UserProfilDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.dto.manga.MangaLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.GenderUser;
import manga_up.manga_up.model.UserAddress;

@ActiveProfiles("test")
public class AppUserMapperTest {


    private AppUserMapper appUserMapper;
    private UserAddressMapper userAddressMapper;
    private GenderUserMapper genderUserMapper;
    private MangaMapper mangaMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private PictureMapper pictureMapper;

    @Mock
    private GenreDao genreDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 

        userAddressMapper = new UserAddressMapper();
        genderUserMapper = new GenderUserMapper();
        mangaMapper = new MangaMapper(categoryMapper, pictureMapper, null, genreDao);
        appUserMapper = new AppUserMapper(mangaMapper, genderUserMapper, userAddressMapper);
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
    
        UserProfilDto userProfilDto  = appUserMapper.toDtoAppUser(appUser);
        assertNotNull(userProfilDto);
        assertEquals("Doe", userProfilDto.getLastname());


        
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
        MangaLightDto mangaLightDto1 = new MangaLightDto(1, "titre");
        MangaLightDto mangaLightDto2 = new MangaLightDto(2, "titre");

        Set<MangaLightDto> mangaLightDtos = Set.of(mangaLightDto1, mangaLightDto2);

        UserProfilDto userProfilDto = new UserProfilDto(
                1,
                "john_doe", 
                "John",
                "Doe",
                "USER", 
                "+33612345678", 
                "john.doe@example.com", 
                "image.com",
                Instant.now(),
                addressDto,
                genderUser,
                mangaLightDtos
        );

        AppUser appUser = appUserMapper.toEntityAppUser(userProfilDto);
        assertNotNull(appUser);
        assertEquals("Doe", appUser.getLastname());
    }


    @Test
    void shouldtoDtoUpdateAppUser() {
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
        appUser.setUrl("www.elair.com");

        UpdateUserDto userProfilDto = appUserMapper.toDtoUpdateAppUser(appUser);
        assertNotNull(userProfilDto);
        assertEquals("Doe", userProfilDto.getLastname());
        assertEquals("www.elair.com", userProfilDto.getUrl());

    }

    @Test
    void shoultoUpdateUserDto() {
        UserAdressDtoUpdate addressDto = new UserAdressDtoUpdate(
                "12 rue de l'exemple",
                "Bâtiment A",
                "Appartement 302",
                "Paris",
                "42230");

        GenderUserDto genderUser = new GenderUserDto(1, "homme");
        UpdateUserDto userProfilDto = new UpdateUserDto(
                1,
                "John",
                "Doe",
                "+33612345678",
                "john.doe@example.com",
                "test.com", 
                addressDto,
                genderUser      );

        AppUser appUser = appUserMapper.toEntityUpdateUserDto(userProfilDto);
        assertNotNull(appUser);
        assertEquals("Doe", appUser.getLastname());
    }    





}
