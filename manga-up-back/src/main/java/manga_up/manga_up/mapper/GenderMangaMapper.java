package manga_up.manga_up.mapper;


import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dto.GenreDto;
import manga_up.manga_up.dto.MangaLightDto;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenderMangaMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(GenderMangaMapper.class);

    private final MangaDao mangaDao;

    public GenderMangaMapper(MangaDao mangaDao) {

        this.mangaDao = mangaDao;
    }

    public GenreDto toDtoGenre(Genre genre) {
        LOGGER.info("Mangas size before mapping: {}", genre.getMangas().size());
       // Set<MangaLightDto> mangaDtos = genre.getMangas().stream()
           //     .map(manga -> new MangaLightDto(manga.getId(), manga.getTitle())) // Utilisation du DTO allégé
             //   .collect(Collectors.toSet());
        return new GenreDto(
                genre.getId(),
                genre.getLabel(),
                genre.getCreatedAt()
               // mangaDtos
        );
    }



    public Genre toEntity(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setLabel(genreDto.getLabel());
        genre.setCreatedAt(genreDto.getCreatedAt());

      //  Set<Manga> mangas = genreDto.getMangas().stream()
        //         .map(mangaDto -> {
        //           return mangaDao.findById(mangaDto.getId()).orElseThrow(() ->
        //                 new RuntimeException("Manga not found for id: " + mangaDto.getId()));
        //       })
        //       .collect(Collectors.toSet());


        //    genre.setMangas(mangas);
        return genre;
    }

}
