package manga_up.manga_up.dao;

import manga_up.manga_up.model.Category;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MangaDaoTest {

    @Autowired
    private MangaDao mangaDao;
    @Autowired
    private CategoryDao categoryDao;

    @Test
    void shouldGetAll() {
    List<Manga> mangas = mangaDao.findAll();

    assertEquals(2, mangas.size());
        assertEquals("Dragon Ball", mangas.get(0).getTitle());
    }

    @Test
    void shouldGetMangaById() {
        Manga manga = mangaDao.findById(1).get();
        assertEquals("Dragon Ball", manga.getTitle());
    }

    @Test
    void shouldSaveManga() {
        Category category = new Category();
        category.setLabel("Manga");
        category.setDescription("Description");
        category.setCreatedAt(Instant.parse("2023-04-10T10:00:00Z"));
        category = categoryDao.save(category);

        Manga manga = new Manga();
        manga.setTitle("Dragon Ball Z");
        manga.setSubtitle("Un voyage épique plus épique");
        manga.setReleaseDate(Instant.parse("2023-04-10T10:00:00Z"));
        manga.setSummary("L’histoire de Goku.");
        manga.setPrice(BigDecimal.valueOf(20.00));
        manga.setPriceHt(BigDecimal.valueOf(18.00));
        manga.setActive(true);
        manga.setInStock(true);
        manga.setIdCategories(category);

        Manga savedManga = mangaDao.save(manga);
        assertEquals("Dragon Ball Z", savedManga.getTitle());
    }


 @Test
    void shouldUpdateManga() {
        Manga manga = mangaDao.findById(1).get();
        manga.setTitle("Dragon Ball Super");

        Manga savedManga = mangaDao.save(manga);
        assertEquals("Dragon Ball Super", savedManga.getTitle());
 }

 @Test
    void shouldDeleteManga() {
        mangaDao.delete(mangaDao.findById(1).get());
        Optional<Manga> foundManga = mangaDao.findById(1);
        assertFalse(foundManga.isPresent());
 }

 @Test
 public void findMangasWithMainPicturesTest() {
     Pageable pageable = PageRequest.of(0, 10);
     Page<MangaProjectionWithAuthor> result = mangaDao.findMangasWithMainPicturesTest(pageable);

     assertThat(result).isNotEmpty();

     for (MangaProjectionWithAuthor manga : result.getContent()) {
         System.out.println("Manga ID: " + manga.getMangaId());
         System.out.println("Title: " + manga.getTitle());
         System.out.println("Authors: " + manga.getAuthors());
         System.out.println("Picture URL: " + manga.getPicture());
         System.out.println("------");
     }
 }





 @Test
 public void shouldfindMangasByGenre2() {
     Pageable pageable = PageRequest.of(0, 10);
     Page<MangaProjectionWithAuthor> result = mangaDao.findMangasByGenre2(1, pageable);

     assertThat(result).isNotEmpty();

     MangaProjectionWithAuthor manga = result.getContent().get(0);
     System.out.println("Manga: " + manga.getTitle());
     System.out.println("Authors: " + manga.getAuthors());
     System.out.println("Picture URL: " + manga.getPicture());

 }



 @Test
 public void testFindMangasByCategory2() {
     Pageable pageable = PageRequest.of(0, 10);
     Page<MangaProjectionWithAuthor> result = mangaDao.findMangasByCategory2(1, pageable);

     assertThat(result).isNotEmpty();

     MangaProjectionWithAuthor manga = result.getContent().get(0);
     System.out.println("Manga: " + manga.getTitle());
     System.out.println("Authors: " + manga.getAuthors());
     System.out.println("Picture URL: " + manga.getPicture());

 }
 
 @Test
 public void testFindMangasByAuthor2() {
     Pageable pageable = PageRequest.of(0, 10);
     Integer authorId = 1; 

     Page<MangaProjectionWithAuthor> result = mangaDao.findMangasByAuthor2(authorId, pageable);

     assertThat(result).isNotEmpty();

     MangaProjectionWithAuthor manga = result.getContent().get(0);
     System.out.println("Manga: " + manga.getTitle());
     System.out.println("Authors: " + manga.getAuthors());
     System.out.println("Picture URL: " + manga.getPicture());
 }
 
}