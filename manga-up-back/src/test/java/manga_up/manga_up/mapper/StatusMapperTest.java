package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.status.StatusDto;
import manga_up.manga_up.model.Status;
@ActiveProfiles("test")
public class StatusMapperTest {

    private StatusMapper statusMapper;

    @BeforeEach
    void setUp() {
        statusMapper = new StatusMapper();
    }

    @Test
    void shouldToEntity() {
        Status status = new Status();
        status.setId(1);
        status.setLabel("en-cours");

        StatusDto statusDto = statusMapper.toEntity(status);

        assertNotNull(statusDto);
        assertEquals(1, statusDto.getId());
        assertEquals("en-cours", statusDto.getLabel());

    }

    @Test
    void shouldToModel() {

     StatusDto statusDto = new StatusDto(1, "En-cours");

     Status status = statusMapper.toModel(statusDto);

     assertNotNull(status);
     assertEquals(1, status.getId());
     assertEquals("En-cours", status.getLabel());

    }

}
