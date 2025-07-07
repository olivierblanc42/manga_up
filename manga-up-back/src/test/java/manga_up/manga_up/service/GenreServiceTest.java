package manga_up.manga_up.service;

import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.genre.GenreWithMangasResponse;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.mapper.GenderMangaMapper;
import manga_up.manga_up.mapper.MangaMapper;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.genre.GenreProjection;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GenreServiceTest {
    @Mock
    private GenreDao genreDao;
    @InjectMocks
    private GenreService genreService;
    @Mock
    private GenderMangaMapper genderMangaMapper;
    @Mock
    private MangaService mangaService;
    @Mock
    private MangaMapper mangaMapper;
    @Mock
    private MangaDao mangaDao;
    @InjectMocks
    private MangaService mangaServiceInjectMock;

    private final class TestGenreProjection implements GenreProjection {
        private Integer id;
        private String label;
        private String url;
        private LocalDateTime created;
        private String descritption;

        public TestGenreProjection(Integer id, String label, String url, LocalDateTime created, String descritption) {
            this.id = id;
            this.label = label;
            this.url = url;
            this.created = created;
            this.descritption = descritption;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public LocalDateTime getCreatedAt() {
            return created;
        }

        @Override
        public String getDescription() {
            return descritption;
        }

    }

    @Test
    void shouldGetAllGenres() {
        Pageable pageable = PageRequest.of(0, 5);

        GenreProjection genreProjection1 = new TestGenreProjection(1, "Genre1", "url", LocalDateTime.now(),
                "description");
        GenreProjection genreProjection2 = new TestGenreProjection(2, "Genre2", "url", LocalDateTime.now(),
                "description");

        Page<GenreProjection> page = new PageImpl<>(List.of(genreProjection1, genreProjection2));
        when(genreDao.findAllByPage(pageable)).thenReturn(page);

        Page<GenreProjection> result = genreService.findAllByGenre(pageable);

        assertThat(result).hasSize(2).containsExactly(genreProjection1, genreProjection2);
    }

    @Test
    void shouldGetGenreById() {
        GenreProjection g = new TestGenreProjection(1, "Genre1", "url", LocalDateTime.now(), "description");

        when(genreDao.findGenreProjectionById(1)).thenReturn(Optional.of(g));

        GenreProjection genre = genreService.findGenreUserById(1);
        assertThat(genre).isEqualTo(g);
    }


    @Test
    void shouldGetAllGenresUsingMockedProjections() {
        Pageable pageable = PageRequest.of(0, 5);

        GenreProjection genreProjection1 =mock(GenreProjection.class);
        GenreProjection genreProjection2 = mock(GenreProjection.class);

        Page<GenreProjection> page = new PageImpl<>(List.of(genreProjection1, genreProjection2));
        when(genreDao.findAllByPage(pageable)).thenReturn(page);

        Page<GenreProjection> result = genreService.findAllByGenre(pageable);

        assertThat(result).hasSize(2).containsExactly(genreProjection1, genreProjection2);
    }

    @Test
    void shouldGetGenreByIdWithoutUsingMockedProjections() {
        GenreProjection g = mock(GenreProjection.class);

        when(genreDao.findGenreProjectionById(1)).thenReturn(Optional.of(g));

        GenreProjection genre = genreService.findGenreUserById(1);
        assertThat(genre).isEqualTo(g);
    }




    @Test
    void shouldGetAllGenreswithoutMockedProjections() {
        Pageable pageable = PageRequest.of(0, 5);

        GenreProjection genreProjection1 = mock(GenreProjection.class);
        GenreProjection genreProjection2 = mock(GenreProjection.class);

        Page<GenreProjection> page = new PageImpl<>(List.of(genreProjection1, genreProjection2));
        when(genreDao.findAllByPage(pageable)).thenReturn(page);

        Page<GenreProjection> result = genreService.findAllByGenre(pageable);

        assertThat(result).hasSize(2).containsExactly(genreProjection1, genreProjection2);
    }

    @Test
    void shouldGetGenreByIdUsingMockedProjections() {
        GenreProjection g = mock(GenreProjection.class);

        when(genreDao.findGenreProjectionById(1)).thenReturn(Optional.of(g));

        GenreProjection genre = genreService.findGenreUserById(1);
        assertThat(genre).isEqualTo(g);
    }

    @Test
    void shouldSaveGenre() {
        GenreDto genreDto = new GenreDto(1,
                "url",
                "baston",
                "description");

        Genre genreEntity = new Genre();
        genreEntity.setId(1);
        genreEntity.setLabel("baston");

        when(genderMangaMapper.toEntity(genreDto)).thenReturn(genreEntity);
        when(genreDao.save(genreEntity)).thenReturn(genreEntity);
        when(genderMangaMapper.toDtoGenre(genreEntity)).thenReturn(genreDto);

        GenreDto result = genreService.save(genreDto);
        assertThat(result).isEqualTo(genreDto);
        assertThat(result).isNotNull();
        assertThat(result.getLabel()).isEqualTo("baston");

    }

    @Test
    void shouldUpdateGenre() {
        int id = 1;

        GenreDto genreDto = new GenreDto(1, "url", "baston", "baston");

        Genre genreEntity = new Genre();
        genreEntity.setId(id);
        genreEntity.setUrl("oldUrl");
        genreEntity.setLabel("oldLabel");
        genreEntity.setDescription("oldDescription");
        genreEntity.setCreatedAt(Instant.now());

        when(genreDao.findGenreById(id)).thenReturn(Optional.of(genreEntity));
        when(genreDao.save(genreEntity)).thenReturn(genreEntity);
        when(genderMangaMapper.toDtoGenre(genreEntity)).thenReturn(genreDto);

        GenreDto result = genreService.updateGenre(id, genreDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(genreDto);
        assertThat(result.getLabel()).isEqualTo("baston");

        verify(genreDao).findGenreById(id);
        verify(genreDao).save(genreEntity);
        verify(genderMangaMapper).toDtoGenre(genreEntity);
    }

    @Test
    void shouldDeleteGenre() {
        Genre genre = new Genre();
        genre.setId(1);
        genre.setLabel("Genre");
        genre.setUrl("null");
        genre.setDescription("null");
        genre.setCreatedAt(Instant.now());
        when(genreDao.findGenreById(1)).thenReturn(Optional.of(genre));
        genreService.deleteGenre(1);
        verify(genreDao).delete(genre);
    }

    @Test
    void shouldgetRandomFourGenres() {
        GenreDto genreDto1 = new GenreDto(1, "url", "baston", "baston");
        GenreDto genreDto2 = new GenreDto(2, "url", "baston", "baston");
        GenreDto genreDto3 = new GenreDto(3, "url", "baston", "baston");
        GenreDto genreDto4 = new GenreDto(4, "url", "baston", "baston");

        List<GenreDto> dtos = List.of(genreDto1, genreDto2, genreDto3, genreDto4);

        when(genreDao.findRandomGenres()).thenReturn(dtos);
        List<GenreDto> result = genreService.getRandomFourGenres();

        assertThat(result).hasSize(4);
    }

    @Test
    void shouldReturnGenreWithMangas() {
        int genreId = 1;
        Pageable pageable = PageRequest.of(0, 5);

        GenreProjection genreProjection = mock(GenreProjection.class);
        when(genreDao.findGenreProjectionById(eq(genreId))).thenReturn(Optional.of(genreProjection));

        MangaProjectionWithAuthor projection1 = mock(MangaProjectionWithAuthor.class);

        Page<MangaProjectionWithAuthor> projectionPage = new PageImpl<>(List.of(projection1), pageable, 1);
        when(mangaDao.findMangasByGenre2(eq(genreId), eq(pageable))).thenReturn(projectionPage);

        MangaDtoRandom dto = new MangaDtoRandom(1, "Naruto", Set.of(), "http://img1");
        when(mangaMapper.mapToDto(eq(projection1))).thenReturn(dto);

        GenreWithMangasResponse response = genreService.getGenreWithMangas(genreId, pageable);

        assertNotNull(response);
        assertEquals(genreProjection, response.getGenre());
        assertEquals(1, response.getMangas().getTotalElements());
        assertEquals("Naruto", response.getMangas().getContent().get(0).getTitle());

        verify(genreDao).findGenreProjectionById(genreId);
        verify(mangaDao).findMangasByGenre2(genreId, pageable);
        verify(mangaMapper).mapToDto(projection1);
    }

    @Test
    void shouldThrowWhenGenreNotFound() {
        int genreId = 404;
        Pageable pageable = PageRequest.of(0, 10);

        when(genreDao.findGenreProjectionById(genreId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> genreService.getGenreWithMangas(genreId, pageable));

        assertEquals("Genre with id 404 not found", exception.getMessage());
        verify(genreDao).findGenreProjectionById(genreId);
    }
}