package manga_up.manga_up.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.author.AuthorWithMangasResponse;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.dto.category.CategoryWithMangaResponse;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.genre.GenreWithMangasResponse;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaDtoOne;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.projection.genre.GenreProjection;
import manga_up.manga_up.projection.manga.MangaBaseProjection;
import manga_up.manga_up.service.AuthorService;
import manga_up.manga_up.service.CategoryService;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.GenreService;
import manga_up.manga_up.service.GenreUserService;
import manga_up.manga_up.service.MangaService;

@WebMvcTest(PublicController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class PublicControllerTest {
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

    // Test pour Manga

    @Test
    void shouldReturnOneManga() throws Exception {
        CategoryDto category = new CategoryDto(
                1,
                "Aventure",
                "https://example.com/category/aventure",
                "Des récits pleins d'action et de rebondissements.");

        GenreDto genre1 = new GenreDto(
                "Shonen",
                "https://example.com/genre/shonen",
                "Genre destiné principalement aux jeunes garçons.");

        GenreDto genre2 = new GenreDto(
                "Fantastique",
                "https://example.com/genre/fantastique",
                "Manga comportant des éléments surnaturels ou magiques.");

        AuthorDtoRandom author1 = new AuthorDtoRandom(
                1,
                "Akira",
                "Toriyama");

        AuthorDtoRandom author2 = new AuthorDtoRandom(
                2,
                "Naoko",
                "Takeuchi");

        Set<GenreDto> genres = new HashSet<>();
        genres.add(genre1);
        genres.add(genre2);

        Set<AuthorDtoRandom> authors = new HashSet<>();
        authors.add(author1);
        authors.add(author2);

        MangaDtoOne mangaDto = new MangaDtoOne(
                1,
                "Dragon Quest",
                "La Quête du Héros",
                "Un jeune garçon part à l’aventure dans un monde fantastique.",
                new BigDecimal("12.99"),
                category,
                genres,
                authors,
                "https://example.com/manga/dragonquest.jpg");

        List<MangaDtoOne> mangasList = List.of(mangaDto);

        when(mangaService.getRandomManga()).thenReturn(mangasList);

        mockMvc.perform(get("/api/public/one"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Dragon Quest"));
    }

    @Test
    void shouldReturnFourMangaReleaseDateRaw() throws Exception {
        Set<AuthorDtoRandom> authors1 = Set.of(
                new AuthorDtoRandom(1, "Akira", "Toriyama"),
                new AuthorDtoRandom(2, "Naoko", "Takeuchi"));

        Set<AuthorDtoRandom> authors2 = Set.of(
                new AuthorDtoRandom(3, "Masashi", "Kishimoto"));

        Set<AuthorDtoRandom> authors3 = Set.of(
                new AuthorDtoRandom(4, "Eiichiro", "Oda"));

        Set<AuthorDtoRandom> authors4 = Set.of(
                new AuthorDtoRandom(5, "Tite", "Kubo"));

        MangaDtoRandom manga1 = new MangaDtoRandom(
                1,
                "Dragon Ball",
                authors1,
                "https://example.com/manga/dragonball.jpg");

        MangaDtoRandom manga2 = new MangaDtoRandom(
                2,
                "Naruto",
                authors2,
                "https://example.com/manga/naruto.jpg");

        MangaDtoRandom manga3 = new MangaDtoRandom(
                3,
                "One Piece",
                authors3,
                "https://example.com/manga/onepiece.jpg");

        MangaDtoRandom manga4 = new MangaDtoRandom(
                4,
                "Bleach",
                authors4,
                "https://example.com/manga/bleach.jpg");
        List<MangaDtoRandom> mangasList = List.of(manga1, manga2, manga3, manga4);

        when(mangaService.getReleaseDateRaw()).thenReturn(mangasList);
        mockMvc.perform(get("/api/public/four"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].title").value("Dragon Ball"))
                .andExpect(jsonPath("$[1].title").value("Naruto"));
        ;
    }

    @Test
    void shouldReturnRandomFourManga() throws Exception {
        Set<AuthorDtoRandom> authors1 = Set.of(
                new AuthorDtoRandom(1, "Akira", "Toriyama"),
                new AuthorDtoRandom(2, "Naoko", "Takeuchi"));

        Set<AuthorDtoRandom> authors2 = Set.of(
                new AuthorDtoRandom(3, "Masashi", "Kishimoto"));

        Set<AuthorDtoRandom> authors3 = Set.of(
                new AuthorDtoRandom(4, "Eiichiro", "Oda"));

        Set<AuthorDtoRandom> authors4 = Set.of(
                new AuthorDtoRandom(5, "Tite", "Kubo"));

        MangaDtoRandom manga1 = new MangaDtoRandom(
                1,
                "Dragon Ball",
                authors1,
                "https://example.com/manga/dragonball.jpg");

        MangaDtoRandom manga2 = new MangaDtoRandom(
                2,
                "Naruto",
                authors2,
                "https://example.com/manga/naruto.jpg");

        MangaDtoRandom manga3 = new MangaDtoRandom(
                3,
                "One Piece",
                authors3,
                "https://example.com/manga/onepiece.jpg");

        MangaDtoRandom manga4 = new MangaDtoRandom(
                4,
                "Bleach",
                authors4,
                "https://example.com/manga/bleach.jpg");
        List<MangaDtoRandom> mangasList = List.of(manga1, manga2, manga3, manga4);

        when(mangaService.getFourMangaRandom()).thenReturn(mangasList);
        mockMvc.perform(get("/api/public/randomFour"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].title").value("Dragon Ball"))
                .andExpect(jsonPath("$[1].title").value("Naruto"));
        ;
    }

    @Test
    void shouldReturnManga() throws Exception {

        CategoryLittleDto category = new CategoryLittleDto(
                1);

        PictureLightDto picture = new PictureLightDto(
                10,
                "https://example.com/images/manga.jpg",
                true);

        UserFavoriteDto favorite = new UserFavoriteDto(
                5,
                "user@example.com");

        MangaDto mangaDto = new MangaDto(
                "One Piece",
                "L'aventure commence",
                Instant.parse("2023-06-05T10:15:30Z"),
                "Un jeune pirate part à la conquête du One Piece.",
                new BigDecimal("8.99"),
                new BigDecimal("10.79"),
                true,
                true,
                category,
                Set.of(1, 2),
                Set.of(3, 4),
                Set.of(picture),
                Set.of(favorite));
        when(mangaService.findMangaDtoById(1)).thenReturn(mangaDto);
        mockMvc.perform(get("/api/public/manga/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("One Piece"))
                .andExpect(jsonPath("$.pictures[0].url").value("https://example.com/images/manga.jpg"));

    }

    @Test
    void shouldReturnAllManga() throws Exception {
        Set<AuthorDtoRandom> authors1 = Set.of(
                new AuthorDtoRandom(1, "Akira", "Toriyama"),
                new AuthorDtoRandom(2, "Naoko", "Takeuchi"));

        Set<AuthorDtoRandom> authors2 = Set.of(
                new AuthorDtoRandom(3, "Masashi", "Kishimoto"));

        Set<AuthorDtoRandom> authors3 = Set.of(
                new AuthorDtoRandom(4, "Eiichiro", "Oda"));

        Set<AuthorDtoRandom> authors4 = Set.of(
                new AuthorDtoRandom(5, "Tite", "Kubo"));

        MangaDtoRandom manga1 = new MangaDtoRandom(
                1,
                "Dragon Ball",
                authors1,
                "https://example.com/manga/dragonball.jpg");

        MangaDtoRandom manga2 = new MangaDtoRandom(
                2,
                "Naruto",
                authors2,
                "https://example.com/manga/naruto.jpg");

        MangaDtoRandom manga3 = new MangaDtoRandom(
                3,
                "One Piece",
                authors3,
                "https://example.com/manga/onepiece.jpg");

        MangaDtoRandom manga4 = new MangaDtoRandom(
                4,
                "Bleach",
                authors4,
                "https://example.com/manga/bleach.jpg");

        Page<MangaDtoRandom> page = new PageImpl<>(List.of(manga1, manga2, manga3, manga4));
        when(mangaService.getPageMangas(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(
                "/api/public/mangas/paginations")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(4))
                .andExpect(jsonPath("$.content[3].title").value("Bleach"))
                .andExpect(jsonPath("$.content[1].authors[0].lastname").value("Masashi"));
    }

    // Test pour auteurs

    @Test
    void shouldReturnAllAuthors() throws Exception {
        AuthorProjection author1 = new TestAuthorProjection(
                1,
                "Akira",
                "Toriyama",
                "Mangaka japonais, créateur de Dragon Ball.",
                LocalDate.of(2023, 5, 12),
                LocalDate.of(2023, 5, 12),
                "url",
                "Homme");
        AuthorProjection author2 = new TestAuthorProjection(
                2,
                "Naoko",
                "Takeuchi",
                "Autrice de Sailor Moon.",
                LocalDate.of(2022, 9, 27),
                LocalDate.of(2023, 5, 12),
                "url",
                "Homme");

        Page<AuthorProjection> page = new PageImpl<>(List.of(author1, author2));
        when(authorService.getAllAuthors(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(
                "/api/public/authors/pagination")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].firstname").value("Akira"))
                .andExpect(jsonPath("$.content[1].lastname").value("Takeuchi"));
    }

    @Test
    void shouldReturnAuthorsByid() throws Exception {
        AuthorProjection author = new TestAuthorProjection(
                1,
                "Akira",
                "Toriyama",
                "Mangaka japonais, créateur de Dragon Ball.",
                LocalDate.of(2023, 5, 12),
                LocalDate.of(2023, 5, 12),
                "url",
                "Homme");

        when(authorService.getAuthorById(1)).thenReturn(author);
        mockMvc.perform(get("/api/public/author/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Akira"))
                .andExpect(jsonPath("$.lastname").value("Toriyama"));

    }

    @Test
    void shouldReturnAuthorWithmanga() throws Exception {
        AuthorProjection author = new TestAuthorProjection(
                1,
                "Akira",
                "Toriyama",
                "Mangaka japonais, créateur de Dragon Ball.",
                LocalDate.of(2023, 5, 12),
                LocalDate.of(2023, 5, 12),
                "url",
                "Homme");

        Set<AuthorDtoRandom> authorsSet = new HashSet<>();
        authorsSet.add(new AuthorDtoRandom(1, "Akira", "Toriyama"));
        authorsSet.add(new AuthorDtoRandom(2, "Naoko", "Takeuchi"));

        MangaDtoRandom manga1 = new MangaDtoRandom(101, "Dragon Ball", authorsSet, "url1");
        MangaDtoRandom manga2 = new MangaDtoRandom(102, "Sailor Moon", authorsSet, "url2");

        List<MangaDtoRandom> mangasList = List.of(manga1, manga2);
        Page<MangaDtoRandom> mangasPage = new PageImpl<>(mangasList);

        AuthorWithMangasResponse response = new AuthorWithMangasResponse(author, mangasPage);
        // Mock du service
        when(authorService.getAuthorWithMangas(any(Integer.class), any(Pageable.class))).thenReturn(response);

        // Appel GET sur le controller
        mockMvc.perform(get("/api/public/author/1/mangas")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author.firstname").value("Akira"))
                .andExpect(jsonPath("$.author.lastname").value("Toriyama"))
                .andExpect(jsonPath("$.mangas.content.length()").value(2))
                .andExpect(jsonPath("$.mangas.content[0].title").value("Dragon Ball"))
                .andExpect(jsonPath("$.mangas.content[1].title").value("Sailor Moon"))
                .andExpect(jsonPath("$.mangas.content[0].authors[0].firstname").value("Takeuchi"));
    }

    // Test pour Genre

    @Test
    void shouldReturnRandomFourGenre() throws Exception {
        GenreDto genre1 = new GenreDto(
                "Shonen",
                "Shonen",
                "Genre destiné principalement aux jeunes garçons.");
        GenreDto genre2 = new GenreDto(
                "Seinen",
                "Seinen",
                "Genre plus mature, souvent destiné à un public adulte.");
        GenreDto genre3 = new GenreDto(
                "Shojo",
                "Shojo",
                "Genre destiné principalement aux jeunes garçons.");
        GenreDto genre4 = new GenreDto(

                "Josei",
                "Josei",
                "Genre plus mature, souvent destiné à un public adulte.");

        List<GenreDto> list = new ArrayList<>();
        list.add(genre1);
        list.add(genre2);
        list.add(genre3);
        list.add(genre4);

        when(genreService.getRandomFourGenres()).thenReturn(list);

        mockMvc.perform(get("/api/public/genres/four"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].label").value("Shonen"))
                .andExpect(jsonPath("$[1].label").value("Seinen"))
                .andExpect(jsonPath("$[2].label").value("Shojo"))
                .andExpect(jsonPath("$[3].label").value("Josei"));
    }

    @Test
    void shouldReturnAllGenre() throws Exception {
        GenreProjection genre1 = new TestGenreProjection(
                1,
                "Shonen",
                "https://example.com/genre/shonen",
                "Genre destiné principalement aux jeunes garçons.",
                LocalDateTime.of(2023, 5, 12, 10, 30));
        GenreProjection genre2 = new TestGenreProjection(
                2,
                "Seinen",
                "https://example.com/genre/seinen",
                "Genre plus mature, souvent destiné à un public adulte.",
                LocalDateTime.of(2022, 11, 3, 14, 45));

        Page<GenreProjection> page = new PageImpl<>(List.of(genre1, genre2));
        when(genreService.findAllByGenre(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(
                "/api/public/genres/pagination")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].label").value("Shonen"))
                .andExpect(jsonPath("$.content[1].label").value("Seinen"));
    }

    @Test
    void shouldReturnGenreWithmanga() throws Exception {
        GenreProjection genre = new TestGenreProjection(
                1,
                "Shonen",
                "https://example.com/genre/shonen",
                "Genre destiné principalement aux jeunes garçons.",
                LocalDateTime.of(2023, 5, 12, 10, 30));

        Set<AuthorDtoRandom> authorsSet = new HashSet<>();
        authorsSet.add(new AuthorDtoRandom(1, "Akira", "Toriyama"));
        authorsSet.add(new AuthorDtoRandom(2, "Naoko", "Takeuchi"));

        MangaDtoRandom manga1 = new MangaDtoRandom(101, "Dragon Ball", authorsSet, "url1");
        MangaDtoRandom manga2 = new MangaDtoRandom(102, "Sailor Moon", authorsSet, "url2");

        List<MangaDtoRandom> mangasList = List.of(manga1, manga2);
        Page<MangaDtoRandom> mangasPage = new PageImpl<>(mangasList);

        GenreWithMangasResponse response = new GenreWithMangasResponse(genre, mangasPage);
        // Mock du service
        when(genreService.getGenreWithMangas(any(Integer.class), any(Pageable.class))).thenReturn(response);

        // Appel GET sur le controller
        mockMvc.perform(get("/api/public/genres/1/mangas")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genre.label").value("Shonen"))
                .andExpect(jsonPath("$.mangas.content.length()").value(2))
                .andExpect(jsonPath("$.mangas.content[0].title").value("Dragon Ball"))
                .andExpect(jsonPath("$.mangas.content[1].title").value("Sailor Moon"));
    }

    // Test pour Category

    @Test
    void shouldReturnAllCategories() throws Exception {
        CategoryProjection category1 = new TestCategoryProjection(
                1,
                "Aventure",
                LocalDateTime.of(2023, 6, 10, 14, 0),
                "Catégorie regroupant les œuvres pleines d’action et de voyages.",
                "https://example.com/category/aventure");

        CategoryProjection category2 = new TestCategoryProjection(
                2,
                "Fantastique",
                LocalDateTime.of(2022, 12, 5, 9, 30),
                "Catégorie centrée sur des éléments surnaturels ou magiques.",
                "https://example.com/category/fantastique");

        Page<CategoryProjection> page = new PageImpl<>(List.of(category1, category2));
        when(categoryService.findAllCategoriesByPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(
                "/api/public/categories/pagination")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].label").value("Aventure"))
                .andExpect(jsonPath("$.content[1].label").value("Fantastique"));
    }

    @Test
    void shouldReturnCategoryWithmanga() throws Exception {
        CategoryProjection category = new TestCategoryProjection(
                1,
                "Aventure",
                LocalDateTime.of(2023, 6, 10, 14, 0),
                "Catégorie regroupant les œuvres pleines d’action et de voyages.",
                "https://example.com/category/aventure");

        Set<AuthorDtoRandom> authorsSet = new HashSet<>();
        authorsSet.add(new AuthorDtoRandom(1, "Akira", "Toriyama"));
        authorsSet.add(new AuthorDtoRandom(2, "Naoko", "Takeuchi"));

        MangaDtoRandom manga1 = new MangaDtoRandom(101, "Dragon Ball", authorsSet, "url1");
        MangaDtoRandom manga2 = new MangaDtoRandom(102, "Sailor Moon", authorsSet, "url2");

        List<MangaDtoRandom> mangasList = List.of(manga1, manga2);
        Page<MangaDtoRandom> mangasPage = new PageImpl<>(mangasList);

        CategoryWithMangaResponse response = new CategoryWithMangaResponse(category, mangasPage);
        // Mock du service
        when(categoryService.getCategoryWithMangas(any(Integer.class), any(Pageable.class))).thenReturn(response);

        // Appel GET sur le controller
        mockMvc.perform(get("/api/public/category/1/mangas")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category.label").value("Aventure"))
                .andExpect(jsonPath("$.mangas.content.length()").value(2))
                .andExpect(jsonPath("$.mangas.content[0].title").value("Dragon Ball"))
                .andExpect(jsonPath("$.mangas.content[1].title").value("Sailor Moon"));
    }

    @Test
    void shouldReturnPageOfMangasSearch() throws Exception {
        MangaBaseProjection manga1 = new MangaBaseProjection() {
            public Integer getId() {
                return 1;
            }

            public String getTitle() {
                return "Dragon Ball";
            }

            public Integer getPictureId() {
                return 10;
            }

            public String getPictureUrl() {
                return "https://example.com/images/dragonball.jpg";
            }

            public String getAuthorFullName() {
                return "Akira Toriyama";
            }
        };

        MangaBaseProjection manga2 = new MangaBaseProjection() {
            public Integer getId() {
                return 2;
            }

            public String getTitle() {
                return "One Piece";
            }

            public Integer getPictureId() {
                return 11;
            }

            public String getPictureUrl() {
                return "https://example.com/images/onepiece.jpg";
            }

            public String getAuthorFullName() {
                return "Eiichiro Oda";
            }
        };

        // Création d'une page avec plusieurs éléments
        Page<MangaBaseProjection> page = new PageImpl<>(List.of(manga1, manga2));

        // Mock du service avec n'importe quel Pageable et la lettre "D"
        when(mangaService.getTitle(eq("D"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/public/search/D")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Dragon Ball"))
                .andExpect(jsonPath("$.content[1].title").value("One Piece"));
    }

}
