package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.*;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaDtoOne;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.mapper.CategoryMapper;
import manga_up.manga_up.mapper.MangaMapper;
import manga_up.manga_up.model.*;
import manga_up.manga_up.projection.manga.MangaBaseProjection;
import manga_up.manga_up.projection.manga.MangaProjection;
import manga_up.manga_up.projection.manga.MangaProjectionOne;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MangaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MangaService.class);

    private final MangaDao mangaDao;
    private final MangaMapper mangaMapper;
    private final PictureDao pictureDao;
    private final CategoryDao categoryDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CategoryMapper categoryMapper;

    public MangaService(MangaDao mangaDao, MangaMapper mangaMapper, PictureDao pictureDao, CategoryDao categoryDao,
            AuthorDao authorDao, GenreDao genreDao, CategoryMapper categoryMapper) {
        this.mangaDao = mangaDao;
        this.mangaMapper = mangaMapper;
        this.pictureDao = pictureDao;
        this.categoryDao = categoryDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Retrieves a paginated page of mangas.
     *
     * @param pageable a {@link Pageable} object containing pagination and sorting
     *                 information
     * @return a {@link Page<MangaBaseProjection>} containing the mangas
     */
    public Page<MangaBaseProjection> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all mangas by Pageable");
        return mangaDao.findAllMangas(pageable);
    }

    /**
     * Finds a manga DTO by its ID.
     *
     * @param id the ID of the manga
     * @return the corresponding {@link MangaDto}
     * @throws EntityNotFoundException if the manga is not found
     */
    public MangaDto findMangaDtoById(Integer id) {
        LOGGER.info("Find manga by id");
        Manga manga = mangaDao.findManga2ById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manga with ID " + id + " not found"));
        return mangaMapper.mangaToMangaDto(manga);
    }

    /**
     * Finds a manga projection by its ID.
     *
     * @param id the ID of the manga
     * @return the corresponding {@link MangaProjection}
     * @throws EntityNotFoundException if the manga is not found
     */
    public MangaProjection findMangaById(Integer id) {
        LOGGER.info("Find manga by id");
        return mangaDao.findMangaById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
    }

    /**
     * Saves a manga.
     *
     * @param mangaDto the manga DTO to save
     * @return the saved {@link MangaDto}
     * @throws IllegalArgumentException if validation fails (e.g. no images,
     *                                  multiple main images)
     */
    @Transactional
    public MangaDto save(MangaDto mangaDto) {

        if (mangaDto.getPictures() == null || mangaDto.getPictures().isEmpty()) {
            throw new IllegalArgumentException("A manga must contain at least one image.");
        }
        long countMain = mangaDto.getPictures().stream().filter(PictureLightDto::getIsMain).count();
        if (countMain == 0) {
            throw new IllegalArgumentException("At least one image must be marked as main.");
        }
        if (countMain > 1) {
            throw new IllegalArgumentException("Only one image can be marked as main.");
        }

  

        Set<Author> authors = mangaDto.getAuthors().stream()
                .map(id -> authorDao.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found")))
                .collect(Collectors.toSet());
        Set<Genre> genres = mangaDto.getGenres().stream()
                .map(id -> genreDao.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found")))
                .collect(Collectors.toSet());

        Manga manga = mangaMapper.mangaToEntity(mangaDto);
        manga.setAuthors(authors);
        manga.setGenres(genres);
        if (manga.getPriceHt() != null) {
            BigDecimal priceHt = manga.getPriceHt();
            BigDecimal tv = priceHt.multiply(new BigDecimal("0.1"));
            manga.setPriceHt(priceHt.add(tv));
            LOGGER.info("Price adjusted with VAT (10%): {} → {}", priceHt, manga.getPriceHt());
        }

        manga.setPictures(new LinkedHashSet<>());

        manga = mangaDao.save(manga);

        int savedCount = 0;
        for (PictureLightDto picDto : mangaDto.getPictures()) {
            LOGGER.info("→ saving picture #{}: url='{}', isMain={}", savedCount, picDto.getUrl(), picDto.getIsMain());
            Picture pic = new Picture();
            pic.setUrl(picDto.getUrl());
            pic.setMain(picDto.getIsMain());
            pic.setIdMangas(manga); 
            pic = pictureDao.save(pic); 
            manga.getPictures().add(pic); 
            savedCount++;
        }


        manga = mangaDao.save(manga);

        return mangaMapper.mangaToMangaDto(manga);
    }

    @Transactional
    public MangaDto update(Integer id, MangaDto mangaDto) {
        LOGGER.info("Update manga with id {}", id);
        Manga existingManga = mangaDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manga with id " + id + " not found"));

        if (mangaDto.getPictures() == null || mangaDto.getPictures().isEmpty()) {
            LOGGER.error("Validation failed: A manga must have at least one image.");
            throw new IllegalArgumentException("A manga must contain at least one image.");
        }

        Set<Author> authors = mangaDto.getAuthors().stream()
                .map(authorId -> authorDao.findById(authorId)
                        .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found")))
                .collect(Collectors.toSet());

        Set<Genre> genres = mangaDto.getGenres().stream()
                .map(genreId -> genreDao.findById(genreId)
                        .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found")))
                .collect(Collectors.toSet());

        existingManga.setTitle(mangaDto.getTitle());
        existingManga.setSubtitle(mangaDto.getSubtitle());
        existingManga.setReleaseDate(mangaDto.getReleaseDate());
        existingManga.setSummary(mangaDto.getSummary());
        existingManga.setPriceHt(mangaDto.getPriceHt());
        existingManga.setPrice(mangaDto.getPrice());
        existingManga.setInStock(mangaDto.getInStock());
        existingManga.setActive(mangaDto.getActive());
        existingManga.setIdCategories(categoryMapper.categoryLittleDto(mangaDto.getIdCategories()));
        existingManga.setAuthors(authors);
        existingManga.setGenres(genres);

        if (existingManga.getPriceHt() != null) {
            BigDecimal priceHt = existingManga.getPriceHt();
            BigDecimal multiplier = new BigDecimal("0.1");
            BigDecimal tvAmount = priceHt.multiply(multiplier);
            existingManga.setPriceHt(priceHt.add(tvAmount));
            LOGGER.info("Price adjusted with VAT (10%): {} → {}", priceHt, existingManga.getPriceHt());
        } else {
            LOGGER.warn("The price excluding VAT is NULL, no VAT calculation carried out!");
        }

        long countMain = mangaDto.getPictures().stream()
                .filter(PictureLightDto::getIsMain)
                .count();

        if (countMain == 0) {
            throw new IllegalArgumentException("At least one image must be marked as main.");
        }
        if (countMain > 1) {
            throw new IllegalArgumentException("Only one image can be marked as main.");
        }

        Set<Picture> pictures = new HashSet<>();
        for (PictureLightDto pictureDto : mangaDto.getPictures()) {
            Picture picture;
            if (pictureDto.getId() == null) {
                picture = new Picture();
                picture.setUrl(pictureDto.getUrl());
                picture.setMain(pictureDto.getIsMain());
                picture.setIdMangas(existingManga);
                Picture saved = pictureDao.save(picture);
                pictures.add(saved);
            } else {
                picture = pictureDao.findById(pictureDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Image with ID " + pictureDto.getId() + " does not exist."));
                picture.setUrl(pictureDto.getUrl());
                picture.setMain(pictureDto.getIsMain());
                pictures.add(picture);
            }
        }
        existingManga.getPictures().clear();
        existingManga.getPictures().addAll(pictures);

        mangaDao.save(existingManga);

        return mangaMapper.mangaToMangaDto(existingManga);
    }
    
    /**
     * Retrieves a list of four random mangas.
     *
     * @return a list of four random {@link MangaDtoRandom}
     */
    public List<MangaDtoRandom> getFourMangaRandom() {
        List<MangaProjectionWithAuthor> projections = mangaDao.findFourMangasRandom();
        return projections.stream()
                .map(mangaMapper::mapToDto)
                .collect(Collectors.toList());

    }

    /**
     * Retrieves mangas sorted by release date in descending order.
     *
     * @return a list of mangas sorted by release date
     */
    public List<MangaDtoRandom> getReleaseDateRaw() {
        List<MangaProjectionWithAuthor> projections = mangaDao.findMangasReleaseDateRaw();
        return projections.stream()
                .map(mangaMapper::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single random manga.
     *
     * @return a one random {@link MangaDtoOne}
     */
    public MangaDtoOne getRandomManga() {
        MangaProjectionOne projection = mangaDao.findRandomOneManga();

        if (projection == null) {
            throw new NoSuchElementException("No manga found");
        }

        return mangaMapper.mapToDto(projection);
    }

    /**
     * Searches mangas by their title.
     *
     * @param letter   the starting letter or substring of the title
     * @param pageable pagination information
     * @return a page of mangas matching the title criteria
     */
    public Page<MangaBaseProjection> getTitle(String letter, Pageable pageable) {
        return mangaDao.findByTitleWithGenres(letter, pageable);
    }

    /**
     * Retrieves a paginated page of mangas with their main pictures.
     *
     * @param pageable pagination information
     * @return a page of {@link MangaDtoRandom}
     */
    public Page<MangaDtoRandom> getPageMangas(Pageable pageable) {
        Page<MangaProjectionWithAuthor> projections = mangaDao.findMangasWithMainPicturesTest(pageable);
        return projections.map(mangaMapper::mapToDto);
    }

    /**
     * Deletes a manga by its ID.
     *
     * @param id the ID of the manga to delete
     * @throws EntityNotFoundException if the manga is not found
     */
    @Transactional
    public void deleteManga(Integer id) {
        LOGGER.info("Deleting Manga by id {}", id);

        Manga manga = mangaDao.findMangaId(id)
                .orElseThrow(() -> new EntityNotFoundException("Manga with id " + id + " not found"));

        Category category = manga.getIdCategories();
        if (category != null && category.getMangas() != null) {
            category.getMangas().remove(manga);
        }
        manga.setIdCategories(null);

        if (manga.getGenres() != null) {
            for (Genre genre : manga.getGenres()) {
                if (genre.getMangas() != null) {
                    genre.getMangas().remove(manga);
                }
            }
            manga.getGenres().clear();
        }

        if (manga.getAuthors() != null) {
            for (Author author : manga.getAuthors()) {
                if (author.getMangas() != null) {
                    author.getMangas().remove(manga);
                }
            }
            manga.getAuthors().clear();
        }

        mangaDao.delete(manga);
    }

}
