package manga_up.manga_up.service;

import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.mapper.MangaMapper;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MangaServiceTest {
@Mock
private MangaDao mangaDao;
@Mock
private MangaMapper mangaMapper;
@InjectMocks
private MangaService mangaService;

private static class TestMangaProjection implements MangaProjection {
    private final  Integer id;
    private final    String title;
    private final   String subtitle;
    private final   LocalDateTime releaseDate;
    private final   BigDecimal price;
    private final  BigDecimal priceHt;
    private final  Boolean inStock;
    private final   Boolean active;
    private final  CategoryLittleProjection idCategories;
    private final  Set<GenreLittleProjection> genres;
    private final Set<AuthorLittleProjection> authors;

    private TestMangaProjection(Integer id, String title, String subtitle, LocalDateTime releaseDate, BigDecimal price, BigDecimal priceHt, Boolean inStock, Boolean active, CategoryLittleProjection idCategories, Set<GenreLittleProjection> genres, Set<AuthorLittleProjection> authors) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.releaseDate = releaseDate;
        this.price = price;
        this.priceHt = priceHt;
        this.inStock = inStock;
        this.active = active;
        this.idCategories = idCategories;
        this.genres = genres;
        this.authors = authors;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public BigDecimal getPriceHt() {
        return priceHt;
    }

    @Override
    public Boolean getInStock() {
        return inStock;
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    @Override
    public CategoryLittleProjection getIdCategories() {
        return idCategories;
    }

    @Override
    public Set<GenreLittleProjection> getGenres() {
        return genres;
    }

    @Override
    public Set<AuthorLittleProjection> getAuthors() {
        return authors;
    }
}

private static class TestCategoryLittleProjection implements CategoryLittleProjection {
    private final   Integer id;
    private final   String label;
    private final LocalDateTime createdAt;

    private TestCategoryLittleProjection(Integer id, String label, LocalDateTime createdAt) {
        this.id = id;
        this.label = label;
        this.createdAt = createdAt;
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

private static class TestAuthorLittleProjection implements AuthorLittleProjection {
    private final   Integer id;
    private final String firstname;
    private final String lastname;

        private TestAuthorLittleProjection(Integer id, String firstname, String lastname) {
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getFirstname() {
            return firstname;
        }

        @Override
        public String getLastname() {
            return lastname;
        }
    }

private static class TestGenreLittleProjection implements GenreLittleProjection {
    private final   Integer id;
    private final String label;
    private final LocalDateTime createdAt;

    private TestGenreLittleProjection(Integer id, String label, LocalDateTime createdAt) {
        this.id = id;
        this.label = label;
        this.createdAt = createdAt;
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}


@Test
    void shouldAllManga() {
    int id = 1;
    Pageable pageable = PageRequest.of(0, 5);

  GenreLittleProjection genreLittleProjection = new TestGenreLittleProjection(id, "genre1", LocalDateTime.now());
  AuthorLittleProjection authorLittleProjection = new TestAuthorLittleProjection(id, "Akira", "Toriyama");
  CategoryLittleProjection categoryLittleProjection = new TestCategoryLittleProjection(id, "category1", LocalDateTime.now());
  Set<AuthorLittleProjection> authors = new HashSet<>();
  authors.add(authorLittleProjection);
  Set<GenreLittleProjection> genres = new HashSet<>();
  genres.add(genreLittleProjection);
  MangaProjection mangaProjection1 = new TestMangaProjection(
          1,
          "Naruto",
          "Test",
          LocalDateTime.now(),
          new BigDecimal("44"),
          new BigDecimal("50"),
          true,
          true,
          categoryLittleProjection,
          genres,
          authors
  );

    MangaProjection mangaProjection2 = new TestMangaProjection(
            1,
            "Naruto",
            "Test",
            LocalDateTime.now(),
            new BigDecimal("44"),
            new BigDecimal("50"),
            true,
            true,
            categoryLittleProjection,
            genres,
            authors
    );

    Page<MangaProjection> page = new PageImpl<>(List.of(mangaProjection1, mangaProjection2));
    when(mangaDao.findAllMangas(pageable)).thenReturn(page);

    Page<MangaProjection> result = mangaDao.findAllMangas(pageable);
    assertThat(result).hasSize(2).containsExactly(mangaProjection1, mangaProjection2);


}

    @Test
  void shouldMangaByid(){
      int id = 1;

      GenreLittleProjection genreLittleProjection = new TestGenreLittleProjection(id, "genre1", LocalDateTime.now());
      AuthorLittleProjection authorLittleProjection = new TestAuthorLittleProjection(id, "Akira", "Toriyama");
      CategoryLittleProjection categoryLittleProjection = new TestCategoryLittleProjection(id, "category1", LocalDateTime.now());
      Set<AuthorLittleProjection> authors = new HashSet<>();
      authors.add(authorLittleProjection);
      Set<GenreLittleProjection> genres = new HashSet<>();
      genres.add(genreLittleProjection);
      MangaProjection mangaProjection1 = new TestMangaProjection(
              1,
              "Naruto",
              "Test",
              LocalDateTime.now(),
              new BigDecimal("44"),
              new BigDecimal("50"),
              true,
              true,
              categoryLittleProjection,
              genres,
              authors
      );

      when(mangaDao.findMangaById(1)).thenReturn(Optional.of(mangaProjection1));
      MangaProjection result = mangaService.findMangaById(1);
      assertThat(result).isEqualTo(mangaProjection1);
  }


  @Test
    void ShouldDeleteManga(){
      int id = 1;
      Genre genre = new Genre();
      genre.setId(id);
      Author author = new Author();
      author.setId(id);

      Set<Author> authors = new HashSet<>();
      authors.add(author);
      Set<Genre> genres = new HashSet<>();
      genres.add(genre);

      Manga manga = new Manga();
    manga.setId(id);
    manga.setPrice(new BigDecimal("44"));
    manga.setTitle("Test");
    manga.setSummary("Test");
    manga.setPriceHt(new BigDecimal("44"));
    manga.setGenres(genres);
    manga.setAuthors(authors);

    when(mangaDao.findMangaId(id)).thenReturn(Optional.of(manga));
    mangaService.deleteManga(id);
    verify(mangaDao).delete(manga);
  }



}