package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.genre.GenreLightDto;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaLightDto;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.model.Picture;

@ActiveProfiles("test")
public class MangaMapperTest {

    private CategoryMapper categoryMapper;
    private PictureMapper pictureMapper;
    private FavoriteMapper appUserMapper;
    private GenreDao genreDao;

    private MangaMapper mangaMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = mock(CategoryMapper.class);
        pictureMapper = mock(PictureMapper.class);
        appUserMapper = mock(FavoriteMapper.class);
        genreDao = mock(GenreDao.class);

        mangaMapper = new MangaMapper(categoryMapper, pictureMapper, appUserMapper, genreDao);
    }

    @Test
    void shoulmangaToMangaDto() {
        // Exemples d'entités correspondantes
        Category categoryEntity = new Category();
        categoryEntity.setId(1);

        Genre genreEntity = new Genre();
        genreEntity.setId(10);
        genreEntity.setLabel("Aventure");

        Picture picture1 = new Picture();
        picture1.setId(1);

        Picture picture2 = new Picture();
        picture2.setId(2);

        Set<Genre> genreEntities = Set.of(genreEntity);
        Set<Picture> pictureEntities = Set.of(picture1, picture2);
        Set<AppUser> appUsers = Set.of();

        Manga manga = new Manga();
        manga.setTitle("One Piece");
        manga.setSubtitle("L’aventure commence");
        manga.setReleaseDate(Instant.parse("1999-10-20T00:00:00Z"));
        manga.setSummary("Un jeune garçon veut devenir roi des pirates.");
        manga.setPriceHt(new BigDecimal("10.00"));
        manga.setInStock(true);
        manga.setActive(true);
        manga.setIdCategories(categoryEntity);
        manga.setGenres(genreEntities);
        manga.setPictures(pictureEntities);
        manga.setAppUsers(appUsers);

        when(categoryMapper.toLittleDtoCategory(categoryEntity)).thenReturn(new CategoryLittleDto(1));

        MangaDto mangaDto = mangaMapper.mangaToMangaDto(manga);

        assertNotNull(mangaDto);
        assertEquals("One Piece", mangaDto.getTitle());
        assertEquals(1, mangaDto.getIdCategories().getId());

    }

    @Test
    void shoultoEntityManga() {
        // Création des DTO utilisés dans MangaDto
        CategoryLittleDto categoryDto = new CategoryLittleDto(1);
        PictureLightDto picDto1 = new PictureLightDto(1, "http://example.com/image1.jpg", true);
        PictureLightDto picDto2 = new PictureLightDto(2, "http://example.com/image2.jpg", false);
        Set<Integer> genreIds = Set.of(10);
        Set<UserFavoriteDto> userFavs = Set.of();

        MangaDto mangaDto = new MangaDto(
                "One Piece",
                "L’aventure commence",
                Instant.parse("1999-10-20T00:00:00Z"),
                "Un jeune garçon veut devenir roi des pirates.",
                new BigDecimal("10.00"),
                null,
                true,
                true,
                categoryDto,
                genreIds,
                Set.of(), // auteurs non utilisés dans ta méthode
                Set.of(picDto1, picDto2),
                userFavs);

        // Mocks des dépendances

        // categoryMapper.categoryLittleDto(...) doit retourner une entité Category
        Category categoryEntity = new Category();
        categoryEntity.setId(1);
        when(categoryMapper.categoryLittleDto(categoryDto)).thenReturn(categoryEntity);

        // genreDao.findById(10) doit retourner un Genre valide
        Genre genreEntity = new Genre();
        genreEntity.setId(10);
        genreEntity.setLabel("Aventure");
        when(genreDao.findById(10)).thenReturn(Optional.of(genreEntity));

        // pictureMapper.toEntityPictures(...) doit retourner un Set de Picture
        Picture picture1 = new Picture();
        picture1.setId(1);
        Picture picture2 = new Picture();
        picture2.setId(2);
        Set<Picture> picturesEntity = Set.of(picture1, picture2);
        when(pictureMapper.toEntityPictures(Set.of(picDto1, picDto2))).thenReturn(picturesEntity);

        // appUserMapper.toEntityAppUserFavoriteSet(...) doit retourner un Set d'AppUser
        Set<AppUser> appUsersEntity = Set.of();
        when(appUserMapper.toEntityAppUserFavoriteSet(userFavs)).thenReturn(appUsersEntity);

        // Appel de la méthode testée
        Manga manga = mangaMapper.mangaToEntity(mangaDto);

        // Vérifications
        assertNotNull(manga);
        assertEquals("One Piece", manga.getTitle());
        assertEquals(categoryEntity, manga.getIdCategories());
        assertEquals(1, manga.getGenres().size());
        assertTrue(manga.getGenres().contains(genreEntity));
        assertEquals(picturesEntity, manga.getPictures());
        assertEquals(appUsersEntity, manga.getAppUsers());
    }

    @Test
    void shouldtoMangaLightDto() {
        Manga manga = new Manga();
        manga.setId(1);
        manga.setTitle("One Piece");
        MangaLightDto mangaLightDto = mangaMapper.toMangaLightDto(manga);
        assertNotNull(mangaLightDto);
        assertEquals("One Piece", mangaLightDto.getTitle());

    }

    @Test
    void shouldParseAuthorsCorrectly() {
        String authorsStr = "1:Eiichiro:Oda|2:Masashi:Kishimoto";

        Set<AuthorDtoRandom> result = mangaMapper.parseAuthors(authorsStr);

        Set<AuthorDtoRandom> expected = Set.of(
                new AuthorDtoRandom(1, "Eiichiro", "Oda"),
                new AuthorDtoRandom(2, "Masashi", "Kishimoto"));

        assertEquals(expected, result);
    }

    @Test
    void shouldParseGenresCorrectly() {
        String genresStr = "someUrl1@Baston@resumé|someUrl2@comédie@resumé";

        Set<GenreDto> result = mangaMapper.parseGenres(genresStr);

        Set<GenreDto> expected = Set.of(
                new GenreDto("someUrl1", "Baston", "resumé"),
                new GenreDto("someUrl2", "comédie", "resumé")      );

        assertEquals(expected, result);
    }


    @Test
    void shouldParseCategoryCorrectly() {
        String categoryStr = "1:shonen:resumé:url.com";

       CategoryDto result = mangaMapper.parseCategory(categoryStr);

      CategoryDto expected = new CategoryDto(1,"shonen","resumé","url.com" );
       assertEquals(expected, result);
    }

}
