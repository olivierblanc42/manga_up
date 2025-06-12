package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.picture.PictureDto;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.model.Picture;
import manga_up.manga_up.model.UserAddress;

@ActiveProfiles("test")
public class PictureMapperTest {
    private PictureMapper pictureMapper;

    @BeforeEach
    void setUp() {
        pictureMapper = new PictureMapper();
    }

    @Test
    void shouldtoPictureDto() {
        Picture picture = new Picture();
        picture.setUrl("picture.com");
        ;

        PictureDto pictureDto = pictureMapper.toPictureDto(picture);

        assertNotNull(pictureDto);
        assertEquals("picture.com", pictureDto.getUrl());
    }

    @Test
    void shouldtotoEntity() {
        PictureDto dto = new PictureDto(
                "picture.com");

        Picture picture = pictureMapper.toEntity(dto);

        assertNotNull(picture);
        assertEquals("picture.com", picture.getUrl());
    }

    @Test
    void shouldtoPictureLightDto() {
        Picture picture = new Picture();
        picture.setUrl("picture.com");
        picture.setId(1);
        picture.setMain(true);
        PictureLightDto pictureDto = pictureMapper.toPictureLightDto(picture);

        assertNotNull(pictureDto);
        assertEquals("picture.com", pictureDto.getUrl());
    }

    @Test
    void shouldtotoEntityPictureLightDto() {
        PictureLightDto dto = new PictureLightDto(
                1,
                "picture.com",
                true);

        Picture picture = pictureMapper.toEntityPicture(dto);

        assertNotNull(picture);
        assertEquals("picture.com", picture.getUrl());
    }


    @Test
    void shoulToPictureLightDtoSet() {
        Picture picture = new Picture();
        picture.setUrl("picture.com");
        picture.setId(1);
        picture.setMain(true);
        Set<Picture> pictures = Set.of(picture);
        Set<PictureLightDto> dtos = pictureMapper.toPictureLightDtoSet(pictures);


        assertNotNull(dtos);
        assertEquals(1, pictures.size());
        boolean found = false;
        for (PictureLightDto dto : dtos) {
            if ("picture.com".equals(dto.getUrl())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    




    @Test
    void shouldtoEntityPicturesSet() {
        PictureLightDto dto1 = new PictureLightDto(
                1,
                "picture.com",
                true);
        PictureLightDto dto2 = new PictureLightDto(
                2,
                "picture.com",
                true);

        Set<PictureLightDto> dtos = Set.of(dto1, dto2);
        Set<Picture> pictures = pictureMapper.toEntityPictures(dtos);
        assertNotNull(pictures);
        assertEquals(2, pictures.size());
        boolean found = false;
        for(Picture picture :pictures){
            if("picture.com".equals(picture.getUrl())){
                found = true;
                break;
            } 
        }
                assertTrue(found);
    }

}
