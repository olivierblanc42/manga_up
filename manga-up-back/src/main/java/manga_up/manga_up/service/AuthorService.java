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
        author.setBirthdate(authorDto.getBirthdate());
        author.setGenre(authorDto.getGenre());
        author.setUrl(authorDto.getUrl());
        authorDao.save(author);
        return authorMapper.toDtoAuthor(author);
    }

public AuthorWithMangasResponse getAuthorWithMangas(Integer authorId, Pageable pageable) {
    // Récupération du genre
    AuthorProjection author = authorDao.findAuthorProjectionById(authorId)
             .orElseThrow(() -> new RuntimeException("Author not found"));

    // Récupération des mangas avec auteurs parsés
    Page<MangaDtoRandom> mangas = findMangasByGenre2(authorId, pageable);

    // Construction de la réponse
    return new AuthorWithMangasResponse(author, mangas);
}

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
                    throw new IllegalArgumentException("Auteur mal formé : " + authorData);
                }
                return new AuthorDtoRandom(
                        Integer.parseInt(parts[0]),
                        parts[2], // lastname
                        parts[1] // firstname
                );
            })
            .collect(Collectors.toSet());
}


}
