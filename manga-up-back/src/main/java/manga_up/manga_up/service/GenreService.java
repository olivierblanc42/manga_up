package manga_up.manga_up.service;


import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;

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

    public GenreProjection findGenreUserById(Integer id) {
        LOGGER.info("Find genre user by id");
        return genreDao.findGenreProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));

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
            throw new RuntimeException("Error saving genre", e);
        }

        GenreDto gDto = genderMangaMapper.toDtoGenre(genre);
        return gDto;
    }

    /**
     * Retrieve Four Genres
     * return a list of four genres
     */
    public List<GenreDto> getRandomFourGenres(){

        return genreDao.findRandomGenres();
    }

    @Transactional

    public void deleteGenre(@PathVariable Integer id) {
        LOGGER.info("Delete genre with id {}", id);
        Genre genre = genreDao.findGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id " + id + " not found"));

        if(!genre.getMangas().isEmpty()){
            throw new EntityNotFoundException("The genre is linked to a manga it cannot be deleted");
        }
        genreDao.delete(genre);
    }



    @Transactional
    /**
     * Updates a manga genre.
     *
     * @param genreId the ID of the genre to update
     * @param genreDto the {@link GenreDto} containing the updated genre information
     * @return the updated genre
     */
    public GenreDto updateGenre(Integer genreId, GenreDto genreDto) {
        Genre genre = genreDao.findGenreById(genreId).
                orElseThrow(() -> new RuntimeException("Genre with ID " + genreId + " not found"));
        genre.setLabel(genreDto.getLabel());
        genreDao.save(genre);
        return genderMangaMapper.toDtoGenre(genre);
     }


}
