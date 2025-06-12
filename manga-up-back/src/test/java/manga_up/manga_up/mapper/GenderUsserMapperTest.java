package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.dto.status.StatusDto;
import manga_up.manga_up.model.GenderUser;
import manga_up.manga_up.model.Status;

@ActiveProfiles("test")
public class GenderUsserMapperTest {
 private GenderUserMapper genderUserMapper;

    @BeforeEach
    void setUp() {
        genderUserMapper = new GenderUserMapper();
    }



    @Test
    void shouldToDTO() {
        GenderUser genderUser = new GenderUser();
        genderUser.setId(1);
        genderUser.setLabel("Homme");

        GenderUserDto genderUserDto = genderUserMapper.toDto(genderUser);

        assertNotNull(genderUserDto);
        assertEquals(1, genderUserDto.getId());
        assertEquals("Homme", genderUserDto.getLabel());

    }




    @Test
    void shouldToModel() {

        GenderUserDto genderUserDto = new GenderUserDto(1, "Homme");

        GenderUser genderUser = genderUserMapper.toEntity(genderUserDto);

        assertNotNull(genderUser);
        assertEquals(1, genderUser.getId());
        assertEquals("Homme", genderUser.getLabel());

    }



}
