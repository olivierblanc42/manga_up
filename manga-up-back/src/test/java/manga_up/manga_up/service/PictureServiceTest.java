package manga_up.manga_up.service;

import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dao.PictureDao;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.mapper.PictureMapper;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.model.Picture;
import manga_up.manga_up.projection.manga.MangaLittleProjection;
import manga_up.manga_up.projection.pictureProjection.PictureLittleProjection;
import manga_up.manga_up.projection.pictureProjection.PictureProjection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PictureServiceTest {

    @Mock
    PictureDao pictureDao;
    @Mock
    PictureMapper pictureMapper;
    @Mock
    MangaDao mangaDao;
    @InjectMocks
    PictureService pictureService;

    private static class TestPictureProjection implements PictureProjection {
        private final Integer id;
        private final String url;
        private final Boolean isMain;
        private final MangaLittleProjection idMangas;

        private TestPictureProjection(Integer id, String url, Boolean isMain, MangaLittleProjection idMangas) {
            this.id = id;
            this.url = url;
            this.idMangas = idMangas;
            this.isMain = isMain;
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
        public Boolean getIsMain() {
            return isMain;
        }

        @Override
        public MangaLittleProjection getIdMangas() {
            return idMangas;
        }
    }

    private static class TestLittleMangaProjection implements MangaLittleProjection {
        private final Integer id;
        private final String title;

        private TestLittleMangaProjection(Integer id, String title, Set<PictureLittleProjection> pictures) {
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
        MangaLittleProjection mangaLittleProjection1 = new TestLittleMangaProjection(
                1, "Naruto", Set.of()

        );
        MangaLittleProjection mangaLittleProjection2 = new TestLittleMangaProjection(
                2, "Bleach",
                Set.of());

        PictureProjection pictureProjection1 = new TestPictureProjection(
                1,
                "www.picture.com",
                true,
                mangaLittleProjection2);
        PictureProjection pictureProjection2 = new TestPictureProjection(
                2,
                "www.picture.fr",
                true,
                mangaLittleProjection1);
        Page<PictureProjection> page = new PageImpl<>(List.of(pictureProjection1, pictureProjection2));
        when(pictureDao.findAllByPage(pageable)).thenReturn(page);

        Page<PictureProjection> result = pictureService.findAllByPage(pageable);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertThat(result).hasSize(2).containsExactly(pictureProjection1, pictureProjection2);

    }

    @Test
    void shouldReturnAllPicturesByPageUsingMockedProjections() {
        Pageable pageable = PageRequest.of(0, 5);

        PictureProjection pictureProjection1 = mock(PictureProjection.class);
        PictureProjection pictureProjection2 = mock(PictureProjection.class);

        Page<PictureProjection> page = new PageImpl<>(List.of(pictureProjection1, pictureProjection2));
        when(pictureDao.findAllByPage(pageable)).thenReturn(page);

        Page<PictureProjection> result = pictureService.findAllByPage(pageable);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertThat(result).hasSize(2).containsExactly(pictureProjection1, pictureProjection2);

    }

    @Test
    void findByIdUsingMockedProjections() {

        PictureProjection pictureProjection = mock(PictureProjection.class);
        when(pictureDao.findPictureProjectionById(1)).thenReturn(Optional.of(pictureProjection));

        PictureProjection result = pictureService.findById(1);
        assertNotNull(result);
        assertThat(result).isEqualTo(pictureProjection);

    }

    @Test
    void findById() {
        MangaLittleProjection mangaLittleProjection1 = new TestLittleMangaProjection(
                1, "Naruto",
                Set.of());
        PictureProjection pictureProjection = new TestPictureProjection(
                1,
                "www.picture.com",
                true,
                mangaLittleProjection1);
        when(pictureDao.findPictureProjectionById(1)).thenReturn(Optional.of(pictureProjection));

        PictureProjection result = pictureService.findById(1);
        assertNotNull(result);
        assertThat(result).isEqualTo(pictureProjection);

    }

    @Test
    void shouldUpdatePictureAndSetAsMain() {
        Integer pictureId = 1;
        Integer mangaId = 42;

        PictureLightDto pictureDto = new PictureLightDto(
                pictureId,
                "image.com/updated",
                true);

        Picture existingPicture = new Picture();
        existingPicture.setId(pictureId);
        existingPicture.setUrl("old-url.com");
        existingPicture.setMain(false);

        Manga manga = new Manga();
        manga.setId(mangaId);
        existingPicture.setIdMangas(manga);

        Picture otherMainPicture = new Picture();
        otherMainPicture.setId(2);
        otherMainPicture.setMain(true);
        otherMainPicture.setIdMangas(manga);

        when(pictureDao.findPictureById(pictureId)).thenReturn(Optional.of(existingPicture));
        when(pictureDao.findByIdMangasIdAndIsMainTrue(mangaId)).thenReturn(List.of(otherMainPicture));
        when(pictureMapper.toPictureLightDto(existingPicture)).thenReturn(pictureDto);

        PictureLightDto result = pictureService.updatePicture(pictureId, pictureDto);

        assertThat(result).isNotNull();
        assertThat(existingPicture.getUrl()).isEqualTo("image.com/updated");
        assertThat(existingPicture.getMain()).isTrue();
        assertThat(otherMainPicture.getMain()).isFalse();

        verify(pictureDao).findPictureById(pictureId);
        verify(pictureDao).findByIdMangasIdAndIsMainTrue(mangaId);
        verify(pictureDao).save(otherMainPicture);
        verify(pictureMapper).toPictureLightDto(existingPicture);
    }

    @Test
    void shouldUpdatePictureWithoutChangingOthersWhenNotMain() {
        Integer pictureId = 3;
        PictureLightDto pictureDto = new PictureLightDto(
                pictureId,
                "image.com/updated",
                false);

        Picture existingPicture = new Picture();
        existingPicture.setId(pictureId);
        existingPicture.setUrl("image.com/original");
        existingPicture.setMain(false);

        Manga manga = new Manga();
        manga.setId(99);
        existingPicture.setIdMangas(manga);

        when(pictureDao.findPictureById(pictureId)).thenReturn(Optional.of(existingPicture));
        when(pictureMapper.toPictureLightDto(existingPicture)).thenReturn(pictureDto);

        PictureLightDto result = pictureService.updatePicture(pictureId, pictureDto);

        assertThat(result).isNotNull();
        assertThat(existingPicture.getUrl()).isEqualTo("image.com/updated");
        assertThat(existingPicture.getMain()).isFalse();

        verify(pictureDao).findPictureById(pictureId);
        verify(pictureMapper).toPictureLightDto(existingPicture);
        verify(pictureDao, never()).findByIdMangasIdAndIsMainTrue(anyInt());
    }

    @Test
    void shouldDeletePicture() {
        Integer pictureId = 3;
        Picture picture = new Picture();
        picture.setId(pictureId);
        picture.setUrl("image.com/original");
        picture.setMain(false);

        Manga manga = new Manga();
        manga.setId(42);
        Set<Picture> pictures = new HashSet<>();
        pictures.add(picture);
        manga.setPictures(pictures);
        picture.setIdMangas(manga);

        when(pictureDao.findById(pictureId)).thenReturn(Optional.of(picture));

        pictureService.deletePictureById(pictureId);
        assertThat(manga.getPictures()).doesNotContain(picture);

        assertThat(manga.getPictures()).doesNotContain(picture);
        verify(pictureDao).findById(pictureId);
        verify(mangaDao).save(manga);
        verify(pictureDao).delete(picture);

    }

}