package manga_up.manga_up.service;

import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dao.PictureDao;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaDtoOne;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.mapper.MangaMapper;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.model.Picture;
import manga_up.manga_up.projection.author.AuthorLittleProjection;
import manga_up.manga_up.projection.category.CategoryLittleProjection;
import manga_up.manga_up.projection.genderUser.GenderUserProjection;
import manga_up.manga_up.projection.genre.GenreLittleProjection;
import manga_up.manga_up.projection.manga.MangaBaseProjection;
import manga_up.manga_up.projection.manga.MangaProjection;
import manga_up.manga_up.projection.manga.MangaProjectionOne;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;
import manga_up.manga_up.projection.pictureProjection.PictureLittleProjection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MangaServiceTest {
    @Mock
    private MangaDao mangaDao;
    @Mock
    private MangaMapper mangaMapper;
    @Mock
    private GenreDao genreDao;
    @Mock
    private PictureDao pictureDao;
    @Mock
    private CategoryDao categoryDao;
    @Mock
    private AuthorDao authorDao;
    @InjectMocks
    private MangaService mangaService;

    private static class TestMangaProjection implements MangaProjection {
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

        private TestMangaProjection(Integer id, String title, String subtitle, String summary,
                LocalDateTime releaseDate,
                BigDecimal price, BigDecimal priceHt, Boolean inStock, Boolean active,
                CategoryLittleProjection idCategories, Set<GenreLittleProjection> genres,
                Set<AuthorLittleProjection> authors,
                Set<PictureLittleProjection> pictures) {
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
            this.pictures = null;
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
        public String getSummary() {
            return "";
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

        @Override
        public Set<PictureLittleProjection> getPictures() {
            return pictures;
        }
    }

    private static class TestMangaBaseProjection implements MangaBaseProjection {
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

    private static class TestCategoryLittleProjection implements CategoryLittleProjection {
        private final Integer id;
        private final String label;
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
        private final Integer id;
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
        private final Integer id;
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

    private static class TestPictureLitleProjection implements PictureLittleProjection {
        private final Integer id;
        private final String url;
        private final Boolean isMain;

        private TestPictureLitleProjection(Integer id, String url, Boolean isMain) {
            this.id = id;
            this.url = url;
            this.isMain = isMain;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public Boolean getIsMain() {
            return isMain;
        }

    }

    @Test
    void shouldAllManga() {
        int id = 1;
        Pageable pageable = PageRequest.of(0, 5);

        PictureLittleProjection pictureLittleProjection = new TestPictureLitleProjection(1, "url", true);
        Set<PictureLittleProjection> pictures = new HashSet<>();
        pictures.add(pictureLittleProjection);

        GenreLittleProjection genreLittleProjection = new TestGenreLittleProjection(id, "genre1", LocalDateTime.now());
        AuthorLittleProjection authorLittleProjection = new TestAuthorLittleProjection(id, "Akira", "Toriyama");

        Set<AuthorLittleProjection> authors = new HashSet<>();
        authors.add(authorLittleProjection);
        Set<GenreLittleProjection> genres = new HashSet<>();
        genres.add(genreLittleProjection);

        MangaBaseProjection mangaBaseProjection1 = new TestMangaBaseProjection(
                1,
                "Naruto",
                2,
                "test",
                "test");

        MangaBaseProjection mangaBaseProjection2 = new TestMangaBaseProjection(
                1,
                "Naruto",
                2,
                "test",
                "test");

        Page<MangaBaseProjection> page = new PageImpl<>(List.of(mangaBaseProjection1, mangaBaseProjection2));
        when(mangaDao.findAllMangas(pageable)).thenReturn(page);

        Page<MangaBaseProjection> result = mangaService.findAllByPage(pageable);

        assertThat(result).hasSize(2).containsExactly(mangaBaseProjection1, mangaBaseProjection2);
        verify(mangaDao).findAllMangas(pageable);
    }

    @Test
    void shouldAllMangaUsingMockedProjections() {

        Pageable pageable = PageRequest.of(0, 5);

        PictureLittleProjection pictureLittleProjection = mock(PictureLittleProjection.class);
        Set<PictureLittleProjection> pictures = new HashSet<>();
        pictures.add(pictureLittleProjection);

        GenreLittleProjection genreLittleProjection = mock(GenreLittleProjection.class);
        AuthorLittleProjection authorLittleProjection = mock(AuthorLittleProjection.class);

        Set<AuthorLittleProjection> authors = new HashSet<>();
        authors.add(authorLittleProjection);
        Set<GenreLittleProjection> genres = new HashSet<>();
        genres.add(genreLittleProjection);

        MangaBaseProjection mangaBaseProjection1 = mock(MangaBaseProjection.class);

        MangaBaseProjection mangaBaseProjection2 = mock(MangaBaseProjection.class);

        Page<MangaBaseProjection> page = new PageImpl<>(List.of(mangaBaseProjection1, mangaBaseProjection2));
        when(mangaDao.findAllMangas(pageable)).thenReturn(page);

        Page<MangaBaseProjection> result = mangaService.findAllByPage(pageable);

        assertThat(result).hasSize(2).containsExactly(mangaBaseProjection1, mangaBaseProjection2);
        verify(mangaDao).findAllMangas(pageable);
    }

    @Test
    void shouldReturnMangaDtoWhenFoundById() {
        int id = 1;

        Manga mangaEntity = new Manga();
        mangaEntity.setId(id);
        mangaEntity.setTitle("One Piece");

        MangaDto mangaDto = new MangaDto(
                "One Piece",
                "Subtitle example",
                Instant.now(),
                "Summary example",
                BigDecimal.valueOf(9.99),
                BigDecimal.valueOf(11.99),
                true,
                true,
                new CategoryLittleDto(1),
                Set.of(1, 2),
                Set.of(1),
                Set.of(),
                Set.of());

        when(mangaDao.findManga2ById(id)).thenReturn(Optional.of(mangaEntity));
        when(mangaMapper.mangaToMangaDto(mangaEntity)).thenReturn(mangaDto);

        MangaDto result = mangaService.findMangaDtoById(id);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("One Piece");
        verify(mangaDao).findManga2ById(id);
        verify(mangaMapper).mangaToMangaDto(mangaEntity);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenMangaNotFound() {
        int id = 999;

        when(mangaDao.findManga2ById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mangaService.findMangaDtoById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Manga with ID " + id + " not found");

        verify(mangaDao).findManga2ById(id);
        verifyNoMoreInteractions(mangaMapper);
    }

    @Test
    void shouldMangaById() {
        int id = 1;

        PictureLittleProjection pictureLittleProjection = new TestPictureLitleProjection(1, "url", true);
        Set<PictureLittleProjection> pictures = new HashSet<>();
        pictures.add(pictureLittleProjection);

        GenreLittleProjection genreLittleProjection = new TestGenreLittleProjection(id, "genre1", LocalDateTime.now());
        AuthorLittleProjection authorLittleProjection = new TestAuthorLittleProjection(id, "Akira", "Toriyama");
        CategoryLittleProjection categoryLittleProjection = new TestCategoryLittleProjection(id, "category1",
                LocalDateTime.now());

        Set<AuthorLittleProjection> authors = new HashSet<>();
        authors.add(authorLittleProjection);
        Set<GenreLittleProjection> genres = new HashSet<>();
        genres.add(genreLittleProjection);

        MangaProjection mangaProjection = new TestMangaProjection(
                id,
                "Naruto",
                "Test",
                "test",
                LocalDateTime.now(),
                new BigDecimal("44"),
                new BigDecimal("50"),
                true,
                true,
                categoryLittleProjection,
                genres,
                authors,
                pictures);

        when(mangaDao.findMangaById(id)).thenReturn(Optional.of(mangaProjection));

        MangaProjection result = mangaService.findMangaById(id);

        assertThat(result).isEqualTo(mangaProjection);
        verify(mangaDao).findMangaById(id);
    }

    @Test
    void shouldMangaByIdUsingMockedProjections() {
        int id = 1;

        PictureLittleProjection pictureLittleProjection = mock(PictureLittleProjection.class);
        Set<PictureLittleProjection> pictures = new HashSet<>();
        pictures.add(pictureLittleProjection);

        GenreLittleProjection genreLittleProjection = mock(GenreLittleProjection.class);
        AuthorLittleProjection authorLittleProjection = mock(AuthorLittleProjection.class);
        Set<AuthorLittleProjection> authors = new HashSet<>();
        authors.add(authorLittleProjection);
        Set<GenreLittleProjection> genres = new HashSet<>();
        genres.add(genreLittleProjection);

        MangaProjection mangaProjection = mock(MangaProjection.class);

        when(mangaDao.findMangaById(id)).thenReturn(Optional.of(mangaProjection));

        MangaProjection result = mangaService.findMangaById(id);

        assertThat(result).isEqualTo(mangaProjection);
        verify(mangaDao).findMangaById(id);
    }

    @Test
    void shouldThrowExceptionWhenMangaNotFound() {
        Integer id = 99;

        when(mangaDao.findMangaById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mangaService.findMangaById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category with id " + id + " not found");

        verify(mangaDao).findMangaById(id);
    }

    @Test
    void deleteManga_existingManga_deletesCorrectly() {
        Manga manga = new Manga();
        Category category = new Category();
        manga.setIdCategories(category);

        Genre genre1 = new Genre();
        genre1.setMangas(new HashSet<>(Set.of(manga)));

        Genre genre2 = new Genre();
        genre2.setMangas(new HashSet<>(Set.of(manga)));
        manga.setGenres(new HashSet<>(Set.of(genre1, genre2)));

        Author author = new Author();
        author.setMangas(new HashSet<>(Set.of(manga)));

        manga.setAuthors(new HashSet<>(Set.of(author)));

        when(mangaDao.findMangaId(1)).thenReturn(Optional.of(manga));

        mangaService.deleteManga(1);

        assertFalse(genre1.getMangas().contains(manga));
        assertFalse(genre2.getMangas().contains(manga));
        assertFalse(author.getMangas().contains(manga));

        verify(mangaDao).delete(manga);
    }




    

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingManga() {
        when(mangaDao.findMangaId(99)).thenReturn(Optional.empty()); 

        assertThrows(EntityNotFoundException.class, () -> mangaService.deleteManga(99));

        verify(mangaDao).findMangaId(99); 
        verifyNoMoreInteractions(mangaDao);
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {
        Integer authorId = 1;
        Integer genreId = 1;
        CategoryLittleDto category = new CategoryLittleDto(1);

        PictureLightDto mainPicture = new PictureLightDto(1, "https://example.com/image1.jpg", true);

        MangaDto mangaDto = new MangaDto(
                "One Piece",
                "L’aventure commence",
                Instant.parse("1999-10-20T00:00:00Z"),
                "Un jeune garçon veut devenir roi des pirates.",
                new BigDecimal("10.00"),
                null,
                true,
                true,
                category,
                Set.of(genreId),
                Set.of(authorId),
                Set.of(mainPicture),
                Set.of());

        when(authorDao.findById(authorId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mangaService.save(mangaDto);
        });

        assertTrue(exception.getMessage().contains("Author with id " + authorId + " not found"));

        verify(authorDao).findById(authorId);
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        Integer authorId = 1;
        Integer genreId = 1;
        CategoryLittleDto category = new CategoryLittleDto(1);

        PictureLightDto mainPicture = new PictureLightDto(1, "https://example.com/image1.jpg", true);

        MangaDto mangaDto = new MangaDto(
                "One Piece",
                "L’aventure commence",
                Instant.parse("1999-10-20T00:00:00Z"),
                "Un jeune garçon veut devenir roi des pirates.",
                new BigDecimal("10.00"),
                null,
                true,
                true,
                category,
                Set.of(genreId),
                Set.of(authorId),
                Set.of(mainPicture),
                Set.of());

        when(authorDao.findById(authorId)).thenReturn(Optional.of(new Author()));

        when(genreDao.findById(genreId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mangaService.save(mangaDto);
        });

        assertTrue(exception.getMessage().contains("Genre not found"));

        verify(genreDao).findById(genreId);
    }

    @Test
    void save_shouldPersistMangaWithValidData() {
        Integer authorId = 1;
        Integer genreId = 1;
        CategoryLittleDto category = new CategoryLittleDto(1);

        PictureLightDto mainPicture = new PictureLightDto(null, "https://example.com/image1.jpg", true);

        MangaDto mangaDto = new MangaDto(
                "One Piece",
                "L’aventure commence",
                Instant.parse("1999-10-20T00:00:00Z"),
                "Un jeune garçon veut devenir roi des pirates.",
                new BigDecimal("10.00"),
                null,
                true,
                true,
                category,
                Set.of(genreId),
                Set.of(authorId),
                Set.of(mainPicture),
                Set.of());

        Author author = new Author();
        author.setId(authorId);
        when(authorDao.findById(authorId)).thenReturn(Optional.of(author));

        Genre genre = new Genre();
        genre.setId(genreId);
        when(genreDao.findById(genreId)).thenReturn(Optional.of(genre));

        Manga mangaEntity = new Manga();
        mangaEntity.setAuthors(Set.of(author));
        mangaEntity.setGenres(Set.of(genre));
        mangaEntity.setPictures(new HashSet<>());
        mangaEntity.setPriceHt(mangaDto.getPriceHt());
        mangaEntity.setTitle(mangaDto.getTitle());
        when(mangaMapper.mangaToEntity(mangaDto)).thenReturn(mangaEntity);

        when(mangaMapper.mangaToMangaDto(any(Manga.class))).thenReturn(mangaDto);

        when(mangaDao.save(any(Manga.class))).thenAnswer(invocation -> {
            Manga m = invocation.getArgument(0);
            m.setId(123);
            return m;
        });

        when(pictureDao.save(any(Picture.class))).thenAnswer(invocation -> {
            Picture p = invocation.getArgument(0);
            p.setId(456);
            return p;
        });

        MangaDto savedMangaDto = mangaService.save(mangaDto);

        assertNotNull(savedMangaDto);
        assertEquals("One Piece", savedMangaDto.getTitle());
        assertFalse(savedMangaDto.getPictures().isEmpty());
        assertTrue(savedMangaDto.getPictures().stream().anyMatch(PictureLightDto::getIsMain));

        verify(authorDao).findById(authorId);
        verify(genreDao).findById(genreId);
        verify(mangaMapper).mangaToEntity(mangaDto);
        verify(mangaMapper).mangaToMangaDto(any(Manga.class));
        verify(mangaDao, atLeastOnce()).save(any(Manga.class));
        verify(pictureDao).save(any(Picture.class));
    }

    @Test
    void save_shouldThrowException_whenPicturesIsNull() {

        CategoryLittleDto category = new CategoryLittleDto(1);

        MangaDto mangaDto = new MangaDto(
                "One Piece",
                "L’aventure commence",
                Instant.parse("1999-10-20T00:00:00Z"),
                "Un jeune garçon veut devenir roi des pirates.",
                new BigDecimal("10.00"),
                null,
                true,
                true,
                category,
                Set.of(1),
                Set.of(1),
                null,
                Set.of());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            mangaService.save(mangaDto);
        });

        assertEquals("A manga must contain at least one image.", thrown.getMessage());
    }

    @Test
    void save_shouldThrowException_whenPicturesIsEmpty() {
        CategoryLittleDto category = new CategoryLittleDto(1);
        Set<PictureLightDto> pictureLightDtos = new HashSet<>(); // vide

        MangaDto mangaDto = new MangaDto(
                "One Piece",
                "L’aventure commence",
                Instant.parse("1999-10-20T00:00:00Z"),
                "Un jeune garçon veut devenir roi des pirates.",
                new BigDecimal("10.00"),
                null,
                true,
                true,
                category,
                Set.of(1),
                Set.of(1),
                pictureLightDtos,
                Set.of());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            mangaService.save(mangaDto);
        });

        assertEquals("A manga must contain at least one image.", thrown.getMessage());
    }

    @Test
    void shouldReturnRandomMangaDtoList() {
        // Arrange
        MangaProjectionWithAuthor projection1 = mock(MangaProjectionWithAuthor.class);
        MangaProjectionWithAuthor projection2 = mock(MangaProjectionWithAuthor.class);

        AuthorDtoRandom authorDtoRandom1 = new AuthorDtoRandom(1, "Masashi", "Kishimoto");
        AuthorDtoRandom authorDtoRandom2 = new AuthorDtoRandom(2, "Tite", "Kubo");

        Set<AuthorDtoRandom> dtoRandoms1 = new HashSet<>();
        dtoRandoms1.add(authorDtoRandom1);
        Set<AuthorDtoRandom> dtoRandoms2 = new HashSet<>();
        dtoRandoms2.add(authorDtoRandom2);

        MangaDtoRandom dto1 = new MangaDtoRandom(1, "Naruto", dtoRandoms1, "https://example.com/naruto.jpg");
        MangaDtoRandom dto2 = new MangaDtoRandom(2, "Bleach", dtoRandoms2, "https://example.com/bleach.jpg");

        List<MangaProjectionWithAuthor> projections = Arrays.asList(projection1, projection2);

        when(mangaDao.findFourMangasRandom()).thenReturn(projections);
        when(mangaMapper.mapToDto(projection1)).thenReturn(dto1);
        when(mangaMapper.mapToDto(projection2)).thenReturn(dto2);

        // Act
        List<MangaDtoRandom> result = mangaService.getFourMangaRandom();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Naruto");
        assertThat(result.get(1).getTitle()).isEqualTo("Bleach");
        assertThat(result.get(0).getAuthors()).containsExactly(authorDtoRandom1);
        assertThat(result.get(1).getAuthors()).containsExactly(authorDtoRandom2);

        verify(mangaDao).findFourMangasRandom();
    }

    @Test
    void shouldReturnGetReleaseDateRaw() {
        // Arrange
        MangaProjectionWithAuthor projection1 = mock(MangaProjectionWithAuthor.class);
        MangaProjectionWithAuthor projection2 = mock(MangaProjectionWithAuthor.class);

        AuthorDtoRandom authorDtoRandom1 = new AuthorDtoRandom(1, "Masashi", "Kishimoto");
        AuthorDtoRandom authorDtoRandom2 = new AuthorDtoRandom(2, "Tite", "Kubo");

        Set<AuthorDtoRandom> dtoRandoms1 = new HashSet<>();
        dtoRandoms1.add(authorDtoRandom1);
        Set<AuthorDtoRandom> dtoRandoms2 = new HashSet<>();
        dtoRandoms2.add(authorDtoRandom2);

        MangaDtoRandom dto1 = new MangaDtoRandom(1, "Naruto", dtoRandoms1, "https://example.com/naruto.jpg");
        MangaDtoRandom dto2 = new MangaDtoRandom(2, "Bleach", dtoRandoms2, "https://example.com/bleach.jpg");

        List<MangaProjectionWithAuthor> projections = Arrays.asList(projection1, projection2);

        when(mangaDao.findMangasReleaseDateRaw()).thenReturn(projections);
        when(mangaMapper.mapToDto(projection1)).thenReturn(dto1);
        when(mangaMapper.mapToDto(projection2)).thenReturn(dto2);

        // Act
        List<MangaDtoRandom> result = mangaService.getReleaseDateRaw();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Naruto");
        assertThat(result.get(1).getTitle()).isEqualTo("Bleach");
        assertThat(result.get(0).getAuthors()).containsExactly(authorDtoRandom1);
        assertThat(result.get(1).getAuthors()).containsExactly(authorDtoRandom2);

        verify(mangaDao).findMangasReleaseDateRaw();
    }

    @Test
    void ShouldMapToDto() {
        MangaProjectionOne projection = mock(MangaProjectionOne.class);

        CategoryDto categoryDto = new CategoryDto(1, "Shonen", "Manga destiné aux jeunes garçons",
                "http://example.com/shonen.jpg");
        GenreDto genreDto1 = new GenreDto(1, "Action", "action", "Manga avec beaucoup d'action");
        GenreDto genreDto2 = new GenreDto(2, "Aventure", "adventure", "Manga d'aventure");

        Set<GenreDto> genreDtos = new HashSet<>();
        genreDtos.add(genreDto1);
        genreDtos.add(genreDto2);

        AuthorDtoRandom authorDtoRandom1 = new AuthorDtoRandom(1, "Eiichiro", "Oda");
        AuthorDtoRandom authorDtoRandom2 = new AuthorDtoRandom(2, "John", "Doe");
        Set<AuthorDtoRandom> authorDtos = new HashSet<>();
        authorDtos.add(authorDtoRandom1);
        authorDtos.add(authorDtoRandom2);

        MangaDtoOne dto = new MangaDtoOne(
                123,
                "One Piece",
                "Le début de l'aventure",
                "Un manga d'aventure épique.",
                new BigDecimal("9.99"),
                categoryDto,
                genreDtos,
                authorDtos,
                "http://example.com/onepiece.jpg");

        when(mangaDao.findRandomOneManga()).thenReturn(projection);
        when(mangaMapper.mapToDto(projection)).thenReturn(dto);

        MangaDtoOne result = mangaService.getRandomManga();

        assertNotNull(result);
        assertEquals(123, result.getId());
        assertEquals("One Piece", result.getTitle());
        assertEquals("Le début de l'aventure", result.getSubtitle());
        assertEquals("Un manga d'aventure épique.", result.getSummary());
        assertEquals(new BigDecimal("9.99"), result.getPrice());
        assertEquals(categoryDto, result.getIdCategories());
        assertEquals(genreDtos, result.getGenres());
        assertEquals(authorDtos, result.getAuthors());
        assertEquals("http://example.com/onepiece.jpg", result.getPicture());

        verify(mangaDao).findRandomOneManga();
        verify(mangaMapper).mapToDto(projection);
    }

}