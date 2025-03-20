package manga_up.manga_up.service;


import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.model.Address;
import manga_up.manga_up.model.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GenreService {

    private static final Logger LOGGER= LoggerFactory.getLogger(GenreService.class);
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    /**
     * Récupère une page paginée de genres.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Address >} contenant les genres
     */
    public Page<Genre> findAllByGenre(Pageable pageable) {
        LOGGER.info("Find all genres by Pageable");
        return genreDao.findAllByPage(pageable);
    }
}
