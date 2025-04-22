package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import manga_up.manga_up.dto.AuthorDto;
import manga_up.manga_up.dto.AuthorWithMangasResponse;
import manga_up.manga_up.mapper.AuthorMapper;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.projection.AuthorProjection;
import manga_up.manga_up.projection.MangaBaseProjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;


@Service

public class  AuthorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorDao authorDao;
    private final AuthorMapper authorMapper;
    private final MangaDao mangaDao;

    public AuthorService(AuthorDao authorDao, AuthorMapper authorMapper,MangaDao mangaDao) {
        this.authorDao = authorDao;
        this.authorMapper = authorMapper;
        this.mangaDao = mangaDao;
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

    public AuthorProjection getAuthorById(Integer id) {
        LOGGER.info("Getting author by id");
        return authorDao.findAuthorProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));
    }

@Transactional
   public void deleteAuthorById(Integer id) {
        LOGGER.info("Deleting author by id");
        Author author = authorDao.findAuthorById(id).
                orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));
        if(!author.getMangas().isEmpty()){
            throw new EntityNotFoundException("The author is linked to mangas it cannot be deleted");
        }
        authorDao.delete(author);
   }

@Transactional
    public AuthorDto save(AuthorDto authorDto) {
        LOGGER.info("Saving author");
        LOGGER.info("author authorDto : {}", authorDto);
        Author author = authorMapper.toEntity(authorDto);
        LOGGER.info("author author : {}", author);
        author.setCreatedAt(LocalDate.now());
        try {
            authorDao.save(author);
        } catch (Exception e) {
            LOGGER.error("Error saving author", e);
            throw new RuntimeException("Error saving author",e);
        }
        
        return authorMapper.toDtoAuthor(author);
    }

@Transactional
    public AuthorDto updateAuthor(Integer authorId, AuthorDto authorDto) {
        Author author = authorDao.findAuthorById(authorId).
                orElseThrow(() -> new RuntimeException("Author with id " + authorId + " not found"));
        LOGGER.info("Updating author");
        author.setFirstname(authorDto.getFirstname());
        author.setLastname(authorDto.getLastname());
        author.setDescription(authorDto.getDescription());
        authorDao.save(author);
        return authorMapper.toDtoAuthor(author);
    }


    public AuthorWithMangasResponse getAuthorWithMangas(Integer authorId, Pageable pageable) {
        AuthorProjection author = authorDao.findAuthorProjectionById(authorId)
            .orElseThrow(() -> new RuntimeException("Author not found"));

        Page<MangaBaseProjection> mangas = mangaDao.findMangasByAuthor(authorId, pageable);

        return new AuthorWithMangasResponse(author, mangas);
    }

}
