package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.genre.GenreLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Genre;

@ActiveProfiles("test")
public class GenderMangaMapperTest {

    private GenderMangaMapper genderMangaMapper;

       @BeforeEach
    void setUp() {
      
        genderMangaMapper = new GenderMangaMapper();
    } 

@Test
void shouldToDtoGenre(){

    Genre genre = new Genre();
    genre.setUrl("null.com");
    genre.setLabel("null");
    genre.setDescritpion("null");

    GenreDto genreDto = genderMangaMapper.toDtoGenre(genre);
    assertNotNull(genreDto);
    assertEquals("null", genreDto.getLabel());
}

@Test
void shouldToEntity() {

    GenreDto genreDto = new GenreDto("null.com","null","null");
  

    Genre genre = genderMangaMapper.toEntity(genreDto);
    assertNotNull(genre);
    assertEquals("null", genreDto.getLabel());
}

@Test
void shouldToGenreLightDto() {

    Genre genre = new Genre();
    genre.setId(1);
    

    GenreLightDto  genreLightDto = genderMangaMapper.toGenreLightDto(genre);
    assertNotNull(genreLightDto);
    assertEquals(1, genreLightDto.getId());
}

@Test
void shouldToEntityGenre() {

    GenreLightDto genreLightDto = new GenreLightDto(1);
  

    Genre genre = genderMangaMapper.toEntityGenre(genreLightDto);
    assertNotNull(genre);
    assertEquals(1, genre.getId());
}



    @Test
    void shoulToLightDtoGenres() {
        Genre genre1 = new Genre();
        genre1.setId(1);

        Genre genre2 = new Genre();
        genre2.setId(12);

        Set<Genre> users = Set.of(genre1, genre2);
        Set<GenreLightDto> dtos = genderMangaMapper.toLightDtoGenres(users);
        assertNotNull(dtos);
        assertEquals(2, dtos.size());

    }

    @Test
    void shouldToEntityGenres() {
        GenreLightDto genreLightDto1 = new GenreLightDto(1);
        GenreLightDto genreLightDto2 = new GenreLightDto(2);

        Set<GenreLightDto> dtosGenre = Set.of(genreLightDto1, genreLightDto2);
        Set<Genre> genres = genderMangaMapper.toEntityGenres(dtosGenre);
        assertNotNull(genres);
        assertEquals(2, genres.size());


    }




}






    

