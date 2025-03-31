package manga_up.manga_up.service;


import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dto.GenreDto;
import manga_up.manga_up.mapper.GenderMangaMapper;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.GenreProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class GenreService {

    private static final Logger LOGGER= LoggerFactory.getLogger(GenreService.class);
    private final GenreDao genreDao;
    private final GenderMangaMapper genderMangaMapper;
    public GenreService(GenreDao genreDao, GenderMangaMapper genderMangaMapper) {
        this.genreDao = genreDao;
        this.genderMangaMapper = genderMangaMapper;
    }

    /**
     * Récupère une page paginée de genres.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Address >} contenant les genres
     */
    public Page<GenreProjection> findAllByGenre(Pageable pageable) {
        LOGGER.info("Find all genres by Pageable");
        return genreDao.findAllByPage(pageable);
    }


    @Transactional
    public GenreDto save(GenreDto genreDto) {
        LOGGER.info("genre genreDto : {}", genreDto);
        Genre genre = genderMangaMapper.toEntity(genreDto);
        LOGGER.info("genre genre : {}", genre);
        genre.setCreatedAt(Instant.now());
        try{
            genre = genreDao.save(genre);

        }catch(Exception e){
            LOGGER.error("Error saving genre: ", e);
            throw new RuntimeException("Error saving genre'", e);
        }

        GenreDto gDto = genderMangaMapper.toDtoGenre(genre);
        return gDto;
    }


}
