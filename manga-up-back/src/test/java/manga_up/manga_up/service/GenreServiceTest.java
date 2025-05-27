package manga_up.manga_up.service;

import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.mapper.GenderMangaMapper;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.genre.GenreProjection;

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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
    @Mock
    private GenreDao genreDao;
    @InjectMocks
    private GenreService genreService;
    @Mock
    private GenderMangaMapper genderMangaMapper;

    private final class TestGenreProjection implements GenreProjection {
        private Integer id;
        private String label;
        private String url;
        private LocalDateTime created;
        private String descritption;
        public TestGenreProjection(Integer id, String label, String url, LocalDateTime created ,String descritption) {
            this.id = id;
            this.label = label;
            this.url =url;
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

     GenreProjection genreProjection1 = new TestGenreProjection(1, "Genre1","url", LocalDateTime.now(),"description");
        GenreProjection genreProjection2 = new TestGenreProjection(2, "Genre2","url", LocalDateTime.now(),
                "description");

     Page<GenreProjection> page = new PageImpl<>(List.of(genreProjection1, genreProjection2));
     when(genreDao.findAllByPage(pageable)).thenReturn(page);

     Page<GenreProjection> result = genreService.findAllByGenre(pageable);


     assertThat(result).hasSize(2).containsExactly(genreProjection1, genreProjection2);

 }

 @Test
    void shouldGetGenreById() {
     GenreProjection g = new TestGenreProjection(1, "Genre1","url", LocalDateTime.now(), "description");

     when(genreDao.findGenreProjectionById(1)).thenReturn(Optional.of(g));

     GenreProjection genre = genreService.findGenreUserById(1);
     assertThat(genre).isEqualTo(g);
 }

@Test
    void shouldSaveGenre() {
       GenreDto genreDto = new GenreDto(
              "url",
             "baston",
             "description"
       );

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

    GenreDto genreDto = new GenreDto("url", "baston", "baston");

    Genre genreEntity = new Genre();
    genreEntity.setId(id);
    genreEntity.setUrl("oldUrl");
    genreEntity.setLabel("oldLabel");
    genreEntity.setDescritpion("oldDescription");
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
void shouldDeleteGenre(){
    Genre genre = new Genre();
    genre.setId(1);
    genre.setLabel("Genre");
    genre.setUrl("null");
    genre.setDescritpion("null");
    genre.setCreatedAt(Instant.now());
    when(genreDao.findGenreById(1)).thenReturn(Optional.of(genre));
    genreService.deleteGenre(1);
    verify(genreDao).delete(genre);
}


}