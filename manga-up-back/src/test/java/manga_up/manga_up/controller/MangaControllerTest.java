package manga_up.manga_up.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.controller.PublicControllerTest.TestMangaProjection;
import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.dto.picture.PictureDto;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.projection.author.AuthorLittleProjection;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.category.CategoryLittleProjection;
import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.projection.genre.GenreLittleProjection;
import manga_up.manga_up.projection.genre.GenreProjection;
import manga_up.manga_up.projection.manga.MangaBaseProjection;
import manga_up.manga_up.projection.manga.MangaProjection;
import manga_up.manga_up.projection.pictureProjection.PictureLittleProjection;
import manga_up.manga_up.service.AuthorService;
import manga_up.manga_up.service.CategoryService;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.GenreService;
import manga_up.manga_up.service.GenreUserService;
import manga_up.manga_up.service.MangaService;

@WebMvcTest(MangaController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class MangaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MangaDao mangaDao;

    @MockitoBean
    private AuthorDao authorDao;
    @MockitoBean
    private MangaService mangaService;

    @MockitoBean
    private AuthorService authorService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private GenreService genreService;
    @MockitoBean
    private GenreUserService genreUserService;
    @MockitoBean
    private CategoryService categoryService;

    public static class TestMangaProjection implements MangaProjection {
        private final Integer id;
        private final String title;
        private final String subtitle;
        private final String summary;
        private final LocalDateTime releaseDate;
        private final BigDecimal price;
        private final BigDecimal priceHt;
        private final Boolean inStock;
        private final Boolean active;
        private final CategoryLittleProjection idCategories;
        private final Set<PictureLittleProjection> pictures;
        private final Set<GenreLittleProjection> genres;
        private final Set<AuthorLittleProjection> authors;

        public TestMangaProjection(Integer id, String title, String subtitle, String summary,
                LocalDateTime releaseDate, BigDecimal price, BigDecimal priceHt,
                Boolean inStock, Boolean active, CategoryLittleProjection idCategories,
                Set<PictureLittleProjection> pictures, Set<GenreLittleProjection> genres,
                Set<AuthorLittleProjection> authors) {
            this.id = id;
            this.title = title;
            this.subtitle = subtitle;
            this.summary = summary;
            this.releaseDate = releaseDate;
            this.price = price;
            this.priceHt = priceHt;
            this.inStock = inStock;
            this.active = active;
            this.idCategories = idCategories;
            this.pictures = pictures;
            this.genres = genres;
            this.authors = authors;
        }

        public Integer getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getSummary() {
            return summary;
        }

        public LocalDateTime getReleaseDate() {
            return releaseDate;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public BigDecimal getPriceHt() {
            return priceHt;
        }

        public Boolean getInStock() {
            return inStock;
        }

        public Boolean getActive() {
            return active;
        }

        public CategoryLittleProjection getIdCategories() {
            return idCategories;
        }

        public Set<PictureLittleProjection> getPictures() {
            return pictures;
        }

        public Set<GenreLittleProjection> getGenres() {
            return genres;
        }

        public Set<AuthorLittleProjection> getAuthors() {
            return authors;
        }
    }

    private static class TestPictureLittleProjection implements PictureLittleProjection {
        private final Integer id;
        private final String url;
        private final Boolean isMain;

        public TestPictureLittleProjection(Integer id, String url, Boolean isMain) {
            this.id = id;
            this.url = url;
            this.isMain = isMain;
        }

        public Integer getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public Boolean getIsMain() {
            return isMain;
        }
    }

    private static class TestGenreLittleProjection implements GenreLittleProjection {
        private final Integer id;
        private final String label;
        private final LocalDateTime createdAt;

        public TestGenreLittleProjection(Integer id, String label, LocalDateTime createdAt) {
            this.id = id;
            this.label = label;
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }

    private static class TestAuthorLittleProjection implements AuthorLittleProjection {
        private final Integer id;
        private final String firstname;
        private final String lastname;

        public TestAuthorLittleProjection(Integer id, String firstname, String lastname) {
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        public Integer getId() {
            return id;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getLastname() {
            return lastname;
        }
    }

    private static class TestCategoryLittleProjection implements CategoryLittleProjection {
        private final Integer id;
        private final String label;
        private final LocalDateTime createdAt;

        public TestCategoryLittleProjection(Integer id, String label, LocalDateTime createdAt) {
            this.id = id;
            this.label = label;
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }

    private static class TestAuthorProjection implements AuthorProjection {
        private final Integer id;
        private final String firstname;
        private final String lastname;
        private final String description;
        private final LocalDate createdAt;
        private final LocalDate birthdate;
        private final String url;
        private final String genre;

        public TestAuthorProjection(Integer id, String firstname, String lastname, String description,
                LocalDate createdAt, LocalDate birthdate, String url, String genre) {
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
            this.description = description;
            this.createdAt = createdAt;
            this.birthdate = birthdate;
            this.url = url;
            this.genre = genre;

        }

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

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public LocalDate getCreatedAt() {
            return createdAt;
        }

        @Override
        public LocalDate getBirthdate() {
            return birthdate;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public String getGenre() {
            return genre;
        }

    }

    private static class TestGenreProjection implements GenreProjection {
        private final Integer id;
        private final String label;
        private final String url;
        private final String description;
        private final LocalDateTime createdAt;

        public TestGenreProjection(Integer id,
                String label,
                String url, String description,
                LocalDateTime createdAt) {
            this.id = id;
            this.label = label;
            this.description = description;
            this.createdAt = createdAt;
            this.url = url;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }

    private static class TestCategoryProjection implements CategoryProjection {
        private final Integer id;
        private final String label;
        private final LocalDateTime createdAt;
        private final String description;
        private final String url;

        public TestCategoryProjection(Integer id, String label, LocalDateTime createdAt, String description,
                String url) {
            this.id = id;
            this.label = label;
            this.createdAt = createdAt;
            this.description = description;
            this.url = url;
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

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }

    public class TestMangaBaseProjection implements MangaBaseProjection {

        private final Integer id;
        private final String title;
        private final Integer pictureId;
        private final String pictureUrl;
        private final String authorFullName;

        public TestMangaBaseProjection(Integer id, String title, Integer pictureId, String pictureUrl,
                String authorFullName) {
            this.id = id;
            this.title = title;
            this.pictureId = pictureId;
            this.pictureUrl = pictureUrl;
            this.authorFullName = authorFullName;
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
        public Integer getPictureId() {
            return pictureId;
        }

        @Override
        public String getPictureUrl() {
            return pictureUrl;
        }

        @Override
        public String getAuthorFullName() {
            return authorFullName;
        }

    }

    @Test
    void shouldgetAllMangas() throws Exception {

        TestMangaBaseProjection manga1 = new TestMangaBaseProjection(
                100,
                "One Piece",
                1234,
                "https://example.com/images/one-piece.jpg",
                "Eiichiro Oda");
        TestMangaBaseProjection manga2 = new TestMangaBaseProjection(
                101,
                "Dr. Slump",
                4321,
                "https://example.com/images/dr-slump.jpg",
                "Akira Toriyama");
        List<MangaBaseProjection> mangas = new ArrayList<>();
        mangas.add(manga1);
        mangas.add(manga2);

        Page<MangaBaseProjection> page = new PageImpl<>(mangas);
        when(mangaService.findAllByPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/mangas/pagination").with(
                csrf())
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[1].title").value("Dr. Slump"))
                .andExpect(jsonPath("$.content[0].title").value("One Piece"));

    }

    @Test
    void shouldReturnManga() throws Exception {

        TestCategoryLittleProjection category = new TestCategoryLittleProjection(
                1, "Shonen", LocalDateTime.now().minusYears(2));

        TestPictureLittleProjection picture1 = new TestPictureLittleProjection(101,
                "https://example.com/img1.jpg",
                true);
        TestPictureLittleProjection picture2 = new TestPictureLittleProjection(102,
                "https://example.com/img2.jpg",
                false);

        Set<PictureLittleProjection> pictures = new LinkedHashSet<>();
        pictures.add(picture1);
        pictures.add(picture2);

        TestGenreLittleProjection genre1 = new TestGenreLittleProjection(201, "Aventure",
                LocalDateTime.now().minusMonths(5));
        Set<GenreLittleProjection> genres = new LinkedHashSet<>();
        genres.add(genre1);

        TestAuthorLittleProjection author1 = new TestAuthorLittleProjection(301, "Eiichiro", "Oda");
        Set<AuthorLittleProjection> authors = new LinkedHashSet<>();
        authors.add(author1);

        TestMangaProjection manga = new TestMangaProjection(
                999,
                "One Piece",
                "Luffy à l'aventure",
                "Un jeune garçon rêve de devenir roi des pirates.",
                LocalDateTime.of(1997, 7, 22, 0, 0),
                new BigDecimal("6.99"),
                new BigDecimal("5.83"),
                true,
                true,
                category,
                pictures,
                genres,
                authors);
        when(mangaService.findMangaById(1)).thenReturn(manga);
        mockMvc.perform(get("/api/mangas/manga/1").with(
                csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("One Piece"))
                .andExpect(jsonPath("$.pictures[0].url").value("https://example.com/img1.jpg"));

    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnCreatedManga() throws Exception {
        String json = """
                {
                   "title": "Fullmetal Alchemist",
                   "subtitle": "La quête de la pierre philosophale",
                   "summary": "Deux frères alchimistes cherchent la vérité dans un monde fantastique.",
                   "releaseDate": "2001-07-12T00:00:00Z",
                   "priceHt": 7.99,
                   "price": 9.59,
                   "inStock": true,
                   "active": false,
                   "idCategories": {
                     "id": 3
                   },
                   "genres": [1, 4],
                   "authors": [10],
                   "pictures": [
                     {
                       "id": 1,
                       "url": "https://example.com/images/fma_cover.jpg",
                       "main": true
                     },
                     {
                       "id": 2,
                       "url": "https://example.com/images/fma_alt.jpg",
                       "main": false
                     }
                   ],
                   "usersFavorites": []
                 }
                 """;

        MangaDto mangaDto = new MangaDto(
                "Fullmetal Alchemist",
                "La quête de la pierre philosophale",
                Instant.parse("2001-07-12T00:00:00Z"),
                "Deux frères alchimistes cherchent la vérité dans un monde fantastique.",
                new BigDecimal("7.99"),
                new BigDecimal("9.59"),
                true,
                false,
                new CategoryLittleDto(3),
                Set.of(1, 4),
                Set.of(10),
                List.of(
                        new PictureLightDto(1, "https://example.com/images/fma_cover.jpg", true),
                        new PictureLightDto(2, "https://example.com/images/fma_alt.jpg", false)),
                Set.of());

        when(mangaService.save(any())).thenReturn(mangaDto);

        mockMvc.perform(post("/api/mangas/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Fullmetal Alchemist"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/mangas/1").with(csrf()))
                .andExpect(status().isOk());

        verify(mangaService).deleteManga(1);
    }

    @Test
    void shouldReturnMangaDtoRandom() throws Exception {
        AuthorDtoRandom author1 = new AuthorDtoRandom(1, "Arakawa", "Hiromu");
        AuthorDtoRandom author2 = new AuthorDtoRandom(2, "Toriyama", "Akira");
        AuthorDtoRandom author3 = new AuthorDtoRandom(3, "Takeuchi", "Naoko");
        AuthorDtoRandom author4 = new AuthorDtoRandom(4, "Oda", "Eiichiro");

        MangaDtoRandom manga1 = new MangaDtoRandom(
                101,
                "Fullmetal Alchemist",
                Set.of(author1),
                "https://example.com/fma.jpg");

        MangaDtoRandom manga2 = new MangaDtoRandom(
                102,
                "Fullmetal Alchemist2",
                Set.of(author2),
                "https://example.com/fma2.jpg");
        MangaDtoRandom manga3 = new MangaDtoRandom(
                104,
                "Sailor Moon",
                Set.of(author3),
                "https://example.com/sailormoon.jpg");

        MangaDtoRandom manga4 = new MangaDtoRandom(
                103,
                "One Piece",
                Set.of(author4),
                "https://example.com/onepiece.jpg");

        List<MangaDtoRandom> list = new ArrayList<>();
        list.add(manga2);
        list.add(manga1);
        list.add(manga3);
        list.add(manga4);
        when(mangaService.getFourMangaRandom()).thenReturn(list);

        mockMvc.perform(get("/api/mangas/randomFour").with(
                csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[1].title").value("Fullmetal Alchemist"))
                .andExpect(jsonPath("$[0].title").value("Fullmetal Alchemist2"));

    }

}
