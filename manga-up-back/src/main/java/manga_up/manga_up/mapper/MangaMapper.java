package manga_up.manga_up.mapper;

import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaLightDto;
import manga_up.manga_up.model.*;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MangaMapper {

    private final CategoryMapper categoryMapper;
    private final GenderMangaMapper genderMapper;
    private final AuthorMapper authorMapper;
    private final PictureMapper pictureMapper;
    private final FavoriteMapper appUserMapper;
    private final GenreDao genreDao;

    public MangaMapper(CategoryMapper categoryMapper, GenderMangaMapper genderMapper, AuthorMapper authorMapper,
            PictureMapper pictureMapper,
            FavoriteMapper appUserMapper, GenreDao genreDao) {
        this.categoryMapper = categoryMapper;
        this.genderMapper = genderMapper;
        this.authorMapper = authorMapper;
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

    public Manga mangaToEntity(MangaDto mangaDto) {
        Manga manga = new Manga();
        manga.setTitle(mangaDto.getTitle());
        manga.setSubtitle(mangaDto.getSubtitle());
        manga.setReleaseDate(mangaDto.getReleaseDate());
        manga.setSummary(mangaDto.getSummary());
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

        Set<AppUser> appUsers = appUserMapper.toEntityAppUserFavorite(mangaDto.getUsersFavorites());
        manga.setAppUsers(appUsers);

        return manga;
    }

    public MangaLightDto toMangaLightDto(Manga manga) {
        return new MangaLightDto(
                manga.getId(),
                manga.getTitle());
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

}
