package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.CategoryLittleDto;
import manga_up.manga_up.dto.MangaDto;
import manga_up.manga_up.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MangaMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(MangaMapper.class);

 private final   CategoryMapper categoryMapper;
    private final   GenderMangaMapper genderMapper;
    private final   AuthorMapper authorMapper;
    private final   PictureMapper pictureMapper;

    public MangaMapper(CategoryMapper categoryMapper, GenderMangaMapper genderMapper, AuthorMapper authorMapper, PictureMapper pictureMapper) {
        this.categoryMapper = categoryMapper;
        this.genderMapper = genderMapper;
        this.authorMapper = authorMapper;
        this.pictureMapper = pictureMapper;
    }


    public MangaDto mangaToMangaDto(Manga manga) {
       return new MangaDto(
                manga.getTitle(),
                manga.getSubtitle(),
                manga.getReleaseDate(),
                manga.getSummary(),
                manga.getPriceHt(),
                manga.getInStock(),
                manga.getActive(),
                categoryMapper.toLittleDtoCategory(manga.getIdCategories()),
                genderMapper.toLightDtoGenres(manga.getGenres()),
                authorMapper.toLigthDtoAuthorSet(manga.getAuthors()),
                pictureMapper.toPictureLightDtoSet(manga.getPictures())
        );
    }

 public Manga mangaToEntity(MangaDto mangaDto) {
        Manga manga = new Manga();
        manga.setTitle(mangaDto.getTitle());
        manga.setSubtitle(mangaDto.getSubtitle());
        manga.setReleaseDate(mangaDto.getReleaseDate());
        manga.setSummary(mangaDto.getSummary());
        manga.setPriceHt(mangaDto.getPriceHt());
        manga.setInStock(mangaDto.getInStock());
        manga.setActive(mangaDto.getActive());
        manga.setIdCategories(categoryMapper.categoryLittleDto(mangaDto.getIdCategories()));


        Set<Genre> genres = genderMapper.toEntityGenres(mangaDto.getGenres());
        manga.setGenres(genres);
        Set<Author> authors = authorMapper.toEntityAuthors(mangaDto.getAuthors());
        manga.setAuthors(authors);
        Set<Picture> pictures = pictureMapper.toEntityPictures(mangaDto.getPictures());
        manga.setPictures(pictures);

        return manga;
 }
}
