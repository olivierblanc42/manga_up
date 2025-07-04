package manga_up.manga_up.dao;

import manga_up.manga_up.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GenreDaoTest {

    @Autowired
  private   GenreDao genreDao;

    @Test
    void shouldGetAllGenres() {
        List<Genre> genres = genreDao.findAll();

        assertEquals(2, genres.size());
        assertEquals("Shonen" , genres.get(0).getLabel());
    }


@Test
    void shouldGetGenreById(){
        Genre genre = genreDao.findById(1).get();
        assertEquals("Shonen" , genre.getLabel());
}

@Test
    void shouldSaveGenre(){
        Genre genre = new Genre();
        genre.setLabel("Shojo");
        genre.setCreatedAt(Instant.parse("2023-04-10T10:00:00Z"));
        genre.setDescription("null");
        genre.setUrl(".com");
        Genre saveGenre = genreDao.save(genre);
        assertNotNull(saveGenre.getId());
        assertEquals("Shojo" , saveGenre.getLabel());
}

@Test
    void shouldUpdateGenre(){
        Genre genre = genreDao.findById(1).get();
        genre.setLabel("Seinen");

        assertEquals("Seinen" , genre.getLabel());
}

@Test
    void shouldDeleteGenre(){
        genreDao.delete(genreDao.findById(1).get());

        Optional<Genre> genre = genreDao.findById(1);
        assertFalse(genre.isPresent());

}

}