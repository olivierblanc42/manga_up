package manga_up.manga_up.dao;

import manga_up.manga_up.model.Author;
import manga_up.manga_up.projection.author.AuthorProjection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AuthorDaoTest {
 @Autowired
 AuthorDao authorDao;

 @Test
 void shouldGetAuthorById() {
     Author author = authorDao.findById(1).get();
     assertEquals("Toriyama", author.getLastname());
 }

    @Test
    void shouldGetAllAuthors() {
        List<Author> authors = authorDao.findAll();

        assertEquals(8, authors.size());
        assertEquals("Toriyama" , authors.get(0).getLastname());
    }



@Test
    void shouldSaveAuthor() {
        Author author = new Author();
        author.setLastname("Takahashi");
        author.setFirstname("Rumiko");
        author.setDescription("Mangaka emblématique, auteure de Inuyasha, Ranma ½ et Maison Ikkoku.");
        author.setCreatedAt(LocalDate.parse("2023-08-21"));

        Author savedAuthor = authorDao.save(author);
        assertEquals("Takahashi", savedAuthor.getLastname());
        assertEquals("Rumiko", savedAuthor.getFirstname());
        assertEquals("Mangaka emblématique, auteure de Inuyasha, Ranma ½ et Maison Ikkoku." ,savedAuthor.getDescription());
        assertEquals(LocalDate.parse("2023-08-21"), savedAuthor.getCreatedAt());
}

@Test
void shouldUpdateAuthor() {
        Author author = authorDao.findById(1).get();
        author.setLastname("Takahashi");

        Author savedAuthor = authorDao.save(author);
        assertEquals("Takahashi", savedAuthor.getLastname());
}


@Test
    void shouldDeleteAuthor() {
 authorDao.delete(authorDao.findById(1).get());
 Optional<Author> author = authorDao.findById(1);
 assertFalse(author.isPresent());

}


@Test
void shouldGetPagedAuthors() {
    Page<AuthorProjection> page = authorDao.findAllByPage(PageRequest.of(0, 5));
    assertFalse(page.isEmpty());
}

@Test
void shouldGetAuthorWithMangasById() {
    Optional<Author> author = authorDao.findAuthorById(1);
    assertTrue(author.isPresent());
    assertNotNull(author.get().getMangas());
}

@Test
void shouldGetAuthorProjectionById() {
    Optional<AuthorProjection> projection = authorDao.findAuthorProjectionById(1);
    assertTrue(projection.isPresent());
    assertEquals("Toriyama", projection.get().getLastname());
}


}