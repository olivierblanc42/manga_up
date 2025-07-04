package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.genre.GenreWithMangasResponse;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.mapper.GenderMangaMapper;
import manga_up.manga_up.mapper.MangaMapper;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(GenreService.class);
    private final GenreDao genreDao;
    private final GenderMangaMapper genderMangaMapper;
    private final MangaDao mangaDao;
    private final MangaMapper mangaMapper;

    public GenreService(GenreDao genreDao, GenderMangaMapper genderMangaMapper, MangaDao mangaDao,
            MangaMapper mangaMapper) {
        this.genreDao = genreDao;
        this.genderMangaMapper = genderMangaMapper;
        this.mangaDao = mangaDao;
        this.mangaMapper =mangaMapper;
    }

    /**
     * Retrieves a paginated list of genres.
     *
     * @param pageable pagination and sorting information
     * @return a page of {@link GenreProjection} objects
     */
    public Page<GenreProjection> findAllByGenre(Pageable pageable) {
        LOGGER.info("Find all genres by Pageable");
        return genreDao.findAllByPage(pageable);
    }

    /**
     * Finds a genre by its ID.
     *
     * @param id the genre ID
     * @return a {@link GenreProjection}
     * @throws EntityNotFoundException if genre not found
     */
    public GenreProjection findGenreUserById(Integer id) {
        LOGGER.info("Find genre by id {}", id);
        return genreDao.findGenreProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id " + id + " not found"));
    }

    /**
     * Saves a new genre.
     *
     * @param genreDto the genre data transfer object
     * @return the saved {@link GenreDto}
     */
    @Transactional
    public GenreDto save(GenreDto genreDto) {
        LOGGER.info("Saving genre: {}", genreDto);
        Genre genre = genderMangaMapper.toEntity(genreDto);
        genre.setCreatedAt(Instant.now());
        try {
            genre = genreDao.save(genre);
        } catch (Exception e) {
            LOGGER.error("Error saving genre: ", e);
            throw new RuntimeException("Error saving genre", e);
        }
        return genderMangaMapper.toDtoGenre(genre);
    }

    /**
     * Retrieves a list of four random genres.
     *
     * @return list of four random {@link GenreDto}
     */
    public List<GenreDto> getRandomFourGenres() {
        return genreDao.findRandomGenres();
    }

    /**
     * Deletes a genre by ID if not linked to mangas.
     *
     * @param id the genre ID
     * @throws EntityNotFoundException if genre not found or linked to mangas
     */
    @Transactional
    public void deleteGenre(@PathVariable Integer id) {
        LOGGER.info("Delete genre with id {}", id);
        Genre genre = genreDao.findGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id " + id + " not found"));

        if (!genre.getMangas().isEmpty()) {
            throw new EntityNotFoundException("The genre is linked to mangas and cannot be deleted");
        }
        genreDao.delete(genre);
    }

    /**
     * Updates a genre.
     *
     * @param genreId  the ID of the genre to update
     * @param genreDto the genre data transfer object with updated data
     * @return the updated {@link GenreDto}
     */
    @Transactional
    public GenreDto updateGenre(Integer genreId, GenreDto genreDto) {
        Genre genre = genreDao.findGenreById(genreId)
                .orElseThrow(() -> new RuntimeException("Genre with ID " + genreId + " not found"));
        genre.setLabel(genreDto.getLabel());
        genre.setDescription(genreDto.getDescription());
        genre.setUrl(genreDto.getUrl());
        genreDao.save(genre);
        return genderMangaMapper.toDtoGenre(genre);
    }

    /**
     * Retrieves a genre with its mangas paginated.
     *
     * @param genreId  the genre ID
     * @param pageable pagination information
     * @return a {@link GenreWithMangasResponse} containing genre and mangas
     */
    public GenreWithMangasResponse getGenreWithMangas(Integer genreId, Pageable pageable) {
        GenreProjection genre = genreDao.findGenreProjectionById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id " + genreId + " not found"));

        Page<MangaDtoRandom> mangas = findMangasByGenre(genreId, pageable);

        return new GenreWithMangasResponse(genre, mangas);
    }

    /**
     * Retrieves paginated mangas for a genre.
     *
     * @param genreId  the genre ID
     * @param pageable pagination info
     * @return paginated {@link MangaDtoRandom}
     */
    public Page<MangaDtoRandom> findMangasByGenre(Integer genreId, Pageable pageable) {
        Page<MangaProjectionWithAuthor> projections = mangaDao.findMangasByGenre2(genreId, pageable);
        return projections.map(mangaMapper::mapToDto);
    }


}
