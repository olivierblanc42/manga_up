package manga_up.manga_up.service;

import manga_up.manga_up.dao.PictureDao;
import manga_up.manga_up.dto.PictureDto;
import manga_up.manga_up.mapper.PictureMapper;
import manga_up.manga_up.model.Picture;
import manga_up.manga_up.projection.AuthorProjection;
import manga_up.manga_up.projection.MangaLittleProjection;
import manga_up.manga_up.projection.PictureProjection;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PictureServiceTest {

 @Mock
 PictureDao pictureDao;
 @Mock
    PictureMapper pictureMapper;
 @InjectMocks
 PictureService pictureService;

 private static class TestPictureProjection implements PictureProjection {
     private final   Integer id;
     private final  String url;
     private final  MangaLittleProjection idMangas;

     private TestPictureProjection(Integer id, String url, MangaLittleProjection idMangas) {
         this.id = id;
         this.url = url;
         this.idMangas = idMangas;
     }

     @Override
     public Integer getId() {
         return id;
     }

     @Override
     public String getUrl() {
         return url;
     }

     @Override
     public MangaLittleProjection getIdMangas() {
         return idMangas;
     }
 }

private static class TestLittleMangaProjection implements MangaLittleProjection {
     private final Integer id;
     private final  String title;

    private TestLittleMangaProjection(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
    @Test
    void shouldReturnAllPicturesByPage() {
        Pageable pageable = PageRequest.of(0, 5);
        MangaLittleProjection mangaLittleProjection1 =  new TestLittleMangaProjection(
                1
                , "Naruto"
        );
        MangaLittleProjection mangaLittleProjection2 =  new TestLittleMangaProjection(
                2
                , "Bleach"
        );

        PictureProjection pictureProjection1 = new TestPictureProjection(
                1,
                "www.picture.com",
                mangaLittleProjection2
        ) ;
        PictureProjection pictureProjection2 = new TestPictureProjection(
                2,
                "www.picture.fr",
                mangaLittleProjection1
        ) ;
        Page<PictureProjection> page = new PageImpl<>(List.of(pictureProjection1, pictureProjection2));
        when(pictureDao.findAllByPage(pageable)).thenReturn(page);

        Page<PictureProjection> result = pictureService.findAllByPage(pageable);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertThat(result).hasSize(2).containsExactly(pictureProjection1, pictureProjection2);

    }

    @Test
    void findById() {
        MangaLittleProjection mangaLittleProjection1 =  new TestLittleMangaProjection(
                1
                , "Naruto"
        );
        PictureProjection pictureProjection = new TestPictureProjection(
                1,
                "www.picture.com",
                mangaLittleProjection1
        ) ;
        when(pictureDao.findPictureProjectionById(1)).thenReturn(Optional.of(pictureProjection));

    PictureProjection result = pictureService.findById(1);
    assertNotNull(result);
    assertThat(result).isEqualTo(pictureProjection);

    }

    @Test
    void updatePicture() {
        int id = 1;

        PictureDto pictureDto = new PictureDto(
         "www.picture.com"
      );
      Picture picture = new Picture();
      picture.setId(id);
      picture.setUrl("www.picture.fr");


      when(pictureDao.findPictureById(1)).thenReturn(Optional.of(picture));
      picture.setUrl(pictureDto.getUrl());
      when(pictureDao.save(picture)).thenReturn(picture);
      when(pictureMapper.toPictureDto(picture)).thenReturn(pictureDto);

      PictureDto result = pictureService.UpdatePicture(id, pictureDto);
      assertNotNull(result);
      assertThat(result.getUrl()).isEqualTo(pictureDto.getUrl());
    }
}