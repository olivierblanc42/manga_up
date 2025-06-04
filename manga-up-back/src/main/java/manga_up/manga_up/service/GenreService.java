package manga_up.manga_up.service;


import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.genre.GenreWithMangasResponse;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.mapper.GenderMangaMapper;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.genre.GenreProjection;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private static final Logger LOGGER= LoggerFactory.getLogger(GenreService.class);
    private final GenreDao genreDao;
    private final GenderMangaMapper genderMangaMapper;
    private final MangaDao mangaDao;

    public GenreService(GenreDao genreDao, GenderMangaMapper genderMangaMapper, MangaDao mangaDao) {
        this.genreDao = genreDao;
        this.genderMangaMapper = genderMangaMapper;
        this.mangaDao = mangaDao;
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
        genre.setDescritpion(genreDto.getDescription());
        genre.setUrl(genreDto.getUrl());
        genreDao.save(genre);
        return genderMangaMapper.toDtoGenre(genre);
     }









public GenreWithMangasResponse getGenreWithMangas(Integer genreId, Pageable pageable) {
    // Récupération du genre
    GenreProjection genre = genreDao.findGenreProjectionById(genreId)
            .orElseThrow(() -> new EntityNotFoundException("Genre with id " + genreId + " not found"));

    // Récupération des mangas avec auteurs parsés
    Page<MangaDtoRandom> mangas = findMangasByGenre2(genreId, pageable);

    // Construction de la réponse
    return new GenreWithMangasResponse(genre, mangas);
}

public Page<MangaDtoRandom> findMangasByGenre2(Integer genreId, Pageable pageable) {
    Page<MangaProjectionWithAuthor> projections = mangaDao.findMangasByGenre2(genreId, pageable);
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
