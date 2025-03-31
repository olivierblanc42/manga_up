package manga_up.manga_up.service;

import manga_up.manga_up.dto.AuthorDto;
import manga_up.manga_up.mapper.AuthorMapper;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.projection.AuthorProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;


@Service

public class AuthorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorDao authorDao;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorDao authorDao, AuthorMapper authorMapper) {
        this.authorDao = authorDao;
        this.authorMapper = authorMapper;
    }

    /**
     * Récupère une page paginée d'authors.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page<AuthorProjection>} contenant les autheurs
     */
    public Page<AuthorProjection> getAllAuthors(Pageable pageable) {
        LOGGER.info("Getting authors");
        return authorDao.findAllByPage(pageable);
    }

    public AuthorDto save(AuthorDto authorDto) {
        LOGGER.info("Saving author");
        Author author = authorMapper.toEntity(authorDto);
        LOGGER.info("genre genreDto : {}", author);
        author.setCreatedAt(LocalDate.now());
        try {
            authorDao.save(author);
        } catch (Exception e) {
            LOGGER.error("Error saving author");
            throw new RuntimeException("Error saving author");
        }
        AuthorDto gDto = authorMapper.toDtoAuthor(author);
        return gDto;

    }
}
