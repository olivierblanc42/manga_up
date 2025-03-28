package manga_up.manga_up.service;

import manga_up.manga_up.model.Author;
import manga_up.manga_up.dao.AuthorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public class AuthorService {
    private static  final Logger LOGGER= LoggerFactory.getLogger(AuthorService.class);
    private final AuthorDao authorDao;

    public AuthorService(AuthorDao authorDao) {
        this.authorDao=authorDao;
    }

    /**
     * Récupère une page paginée d'authors.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page<Author>} contenant les autheurs
     */
    public Page<Author> getAuthors(Pageable pageable) {
        LOGGER.info("Getting authors");
        return authorDao.findAll(pageable);
    }
}
