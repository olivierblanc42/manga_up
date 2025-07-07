package manga_up.manga_up.mapper;

import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaDtoOne;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.dto.manga.MangaLightDto;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.model.*;
import manga_up.manga_up.projection.manga.MangaProjectionOne;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MangaMapper {

    private final CategoryMapper categoryMapper;

    private final PictureMapper pictureMapper;
    private final FavoriteMapper appUserMapper;
    private final GenreDao genreDao;

    public MangaMapper(CategoryMapper categoryMapper,
            PictureMapper pictureMapper,
            FavoriteMapper appUserMapper, GenreDao genreDao) {
        this.categoryMapper = categoryMapper;
        this.pictureMapper = pictureMapper;
        this.appUserMapper = appUserMapper;
        this.genreDao = genreDao;
    }

    public MangaDto mangaToMangaDto(Manga manga) {

        Set<Integer> authorsIds = Collections.emptySet();
        if (manga.getAuthors() != null) {
            authorsIds = manga.getAuthors()
                    .stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet());
        }
        // Set<Integer> authorsIds = Collections.emptySet();

        // if (manga.getAuthors() != null) {
        // Set<Author> authors = manga.getAuthors();
        // Set<Integer> ids = new HashSet<>();

        // for (Author author : authors) {
        // ids.add(author.getId());
        // }

        // authorsIds = ids;
        // }
        Set<Integer> genresIds = Collections.emptySet();
        if (manga.getGenres() != null) {
            genresIds = manga.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet());
        }

        return new MangaDto(
                manga.getTitle(),
                manga.getSubtitle(),
                manga.getReleaseDate(),
                manga.getSummary(),
                manga.getPriceHt(),
                manga.getPrice(),
                manga.getInStock(),
                manga.getActive(),
                categoryMapper.toLittleDtoCategory(manga.getIdCategories()),
                genresIds,
                authorsIds,
                pictureMapper.toPictureLightDtoSet(manga.getPictures()),
                appUserMapper.toUserFavoriteDtoSet(manga.getAppUsers())

        );
    }
    // picture.setUrl(Jsoup.clean(pictureLightDto.getUrl(),Safelist.none()));

    public Manga mangaToEntity(MangaDto mangaDto) {
        Manga manga = new Manga();
        manga.setTitle(Jsoup.clean(mangaDto.getTitle(),Safelist.none()));
        manga.setSubtitle(Jsoup.clean(mangaDto.getSubtitle(), Safelist.none()));
        manga.setReleaseDate(mangaDto.getReleaseDate());
        manga.setSummary(Jsoup.clean(mangaDto.getSummary(), Safelist.none()));
        manga.setPriceHt(mangaDto.getPriceHt());
        manga.setPrice(mangaDto.getPrice());
        manga.setInStock(mangaDto.getInStock());
        manga.setActive(mangaDto.getActive());
        manga.setIdCategories(categoryMapper.categoryLittleDto(mangaDto.getIdCategories()));

        Set<Genre> genres = mangaDto.getGenres().stream()
                .map(genreId -> genreDao.findById(genreId)
                        .orElseThrow(() -> new RuntimeException("Genre not found: " + genreId)))
                .collect(Collectors.toSet());
        manga.setGenres(genres);

        Set<Picture> pictures = pictureMapper.toEntityPictures(mangaDto.getPictures());
        manga.setPictures(pictures);

        Set<AppUser> appUsers = appUserMapper.toEntityAppUserFavoriteSet(mangaDto.getUsersFavorites());
        manga.setAppUsers(appUsers);

        return manga;
    }

    public MangaLightDto toMangaLightDto(Manga manga) {
        PictureLightDto mainPicture = manga.getPictures().stream()
                .filter(Picture::getMain)
                .findFirst()
                .map(pic -> new PictureLightDto(
                        pic.getId(),
                        pic.getUrl(),
                        pic.getMain()))
                .orElse(null);

        return new MangaLightDto(
                manga.getId(),
                manga.getTitle(),
                mainPicture);
    }    


    public Set<MangaLightDto> toMangaLightDtoSet(Set<Manga> mangas) {
        return mangas.stream()
                .map(this::toMangaLightDto)
                .collect(Collectors.toSet());
    }

    public Manga toEntityManga(MangaLightDto mangaLightDto) {
        Manga manga = new Manga();
        manga.setId(mangaLightDto.getId());
        manga.setTitle(mangaLightDto.getTitle());
        return manga;
    }

    public Set<Manga> toEntityMangas(Set<MangaLightDto> mangas) {
        return mangas.stream()
                .map(this::toEntityManga)
                .collect(Collectors.toSet());
    }

    public MangaDtoRandom mapToDto(MangaProjectionWithAuthor projection) {
        Set<AuthorDtoRandom> authors = parseAuthors(projection.getAuthors());

        return new MangaDtoRandom(
                projection.getMangaId(),
                projection.getTitle(),
                authors,
                projection.getPicture());

    }

    // avoir ci je met ses fonction dans un autre fichier
    public MangaDtoOne mapToDto(MangaProjectionOne p) {
        if (p == null)
            return null;
        return new MangaDtoOne(
                p.getId_mangas(),
                p.getTitle(),
                p.getSubtitle(),
                p.getSummary(),
                p.getPrice(),
                parseCategory(p.getCategory()),
                parseGenres(p.getGenres()),
                parseAuthors(p.getAuthors()),
                p.getPicture());
    }

    public CategoryDto parseCategory(String cat) {
        if (cat == null || cat.isEmpty())
            return null;

        String[] parts = cat.split(":");
        return new CategoryDto(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                parts[3]);
    }

    public Set<GenreDto> parseGenres(String genres) {
        if (genres == null || genres.isEmpty())
            return Collections.emptySet();

        return Arrays.stream(genres.split("\\|"))
                .map(s -> {
                    String[] parts = s.split("@");
                    return new GenreDto(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]);
                })
                .collect(Collectors.toSet());
    }

    public Set<AuthorDtoRandom> parseAuthors(String authors) {
        if (authors == null || authors.isEmpty())
            return Collections.emptySet();

        return Arrays.stream(authors.split("\\|"))
                .map(s -> {
                    String[] parts = s.split(":");
                    return new AuthorDtoRandom(Integer.parseInt(parts[0]), parts[1], parts[2]);
                })
                .collect(Collectors.toSet());
    }

}
