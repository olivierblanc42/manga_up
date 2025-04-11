package manga_up.manga_up.dao;

import manga_up.manga_up.model.Category;
import manga_up.manga_up.model.Manga;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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
}