package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.author.AuthorWithMangasResponse;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.mapper.AuthorMapper;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class that manages author-related operations.
 */
@Service
public class AuthorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorDao authorDao;
    private final AuthorMapper authorMapper;
    private final MangaDao mangaDao;

    public AuthorService(AuthorDao authorDao, AuthorMapper authorMapper, MangaDao mangaDao) {
        this.authorDao = authorDao;
        this.authorMapper = authorMapper;
        this.mangaDao = mangaDao;
    }

    /**
     * Retrieves a paginated list of authors.
     *
     * @param pageable Pagination and sorting information.
     * @return A page of author projections.
     */
    public Page<AuthorProjection> getAllAuthors(Pageable pageable) {
        LOGGER.info("Getting authors");
        return authorDao.findAllByPage(pageable);
    }

    /**
     * Retrieves an author by their ID.
     *
     * @param id The ID of the author.
     * @return The author projection.
     * @throws EntityNotFoundException if the author is not found.
     */
    public AuthorProjection getAuthorById(Integer id) {
        LOGGER.info("Getting author by id");
        return authorDao.findAuthorProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));
    }

    /**
     * Deletes an author by their ID if they have no linked mangas.
     *
     * @param id The ID of the author to delete.
     * @throws EntityNotFoundException if the author does not exist or has linked
     *                                 mangas.
     */
    @Transactional
    public void deleteAuthorById(Integer id) {
        LOGGER.info("Deleting author by id");
        Author author = authorDao.findAuthorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));
        if (!author.getMangas().isEmpty()) {
            throw new EntityNotFoundException("The author is linked to mangas and cannot be deleted");
        }
        authorDao.delete(author);
    }

    /**
     * Saves a new author.
     *
     * @param authorDto The author data transfer object.
     * @return The saved author as a DTO.
     */
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
            throw new RuntimeException("Error saving author", e);
        }

        return authorMapper.toDtoAuthor(author);
    }

    /**
     * Updates an existing author by ID.
     *
     * @param authorId  The ID of the author to update.
     * @param authorDto The new data for the author.
     * @return The updated author as a DTO.
     * @throws RuntimeException if the author is not found.
     */
    @Transactional
    public AuthorDto updateAuthor(Integer authorId, AuthorDto authorDto) {
        Author author = authorDao.findAuthorById(authorId)
                .orElseThrow(() -> new RuntimeException("Author with id " + authorId + " not found"));
        LOGGER.info("Updating author");
        author.setFirstname(authorDto.getFirstname());
        author.setLastname(authorDto.getLastname());
        author.setDescription(authorDto.getDescription());
        author.setBirthdate(authorDto.getBirthdate());
        author.setGenre(authorDto.getGenre());
        author.setUrl(authorDto.getUrl());
        authorDao.save(author);
        return authorMapper.toDtoAuthor(author);
    }

    /**
     * Retrieves an author with their mangas in a paginated format.
     *
     * @param authorId The ID of the author.
     * @param pageable Pagination information.
     * @return An AuthorWithMangasResponse containing the author and their mangas.
     */
    public AuthorWithMangasResponse getAuthorWithMangas(Integer authorId, Pageable pageable) {
        AuthorProjection author = authorDao.findAuthorProjectionById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Page<MangaDtoRandom> mangas = findMangasByGenre2(authorId, pageable);

        return new AuthorWithMangasResponse(author, mangas);
    }

    /**
     * Finds mangas by the author's genre with pagination.
     *
     * @param genreId  The ID of the genre.
     * @param pageable Pagination information.
     * @return A page of MangaDtoRandom objects.
     */
    public Page<MangaDtoRandom> findMangasByGenre2(Integer genreId, Pageable pageable) {
        Page<MangaProjectionWithAuthor> projections = mangaDao.findMangasByAuthor2(genreId, pageable);
        return projections.map(this::mapToDto2);
    }

    private MangaDtoRandom mapToDto2(MangaProjectionWithAuthor projection) {
        Set<AuthorDtoRandom> authors = parseAuthors(projection.getAuthors());

        return new MangaDtoRandom(
                projection.getMangaId(),
                projection.getTitle(),
                authors,
                projection.getPicture());
    }

    private Set<AuthorDtoRandom> parseAuthors(String authorsString) {
        if (authorsString == null || authorsString.isEmpty()) {
            return Set.of();
        }

        return Arrays.stream(authorsString.split("\\|"))
                .map(authorData -> {
                    String[] parts = authorData.split(":");
                    if (parts.length < 3) {
                        throw new IllegalArgumentException("Malformed author: " + authorData);
                    }
                    return new AuthorDtoRandom(
                            Integer.parseInt(parts[0]),
                            parts[2], 
                            parts[1] 
                    );
                })
                .collect(Collectors.toSet());
    }
}
