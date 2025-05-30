package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.*;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.genre.GenreLightDto;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaDtoOne;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.mapper.MangaMapper;
import manga_up.manga_up.model.*;
import manga_up.manga_up.projection.manga.MangaBaseProjection;
import manga_up.manga_up.projection.manga.MangaProjection;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

    public MangaService(MangaDao mangaDao, MangaMapper mangaMapper, PictureDao pictureDao, CategoryDao categoryDao,
            AuthorDao authorDao, GenreDao genreDao) {
        this.mangaDao = mangaDao;
        this.mangaMapper = mangaMapper;
        this.pictureDao = pictureDao;
        this.categoryDao = categoryDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    /**
     * Récupère une page paginée de mangas.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de
     *                 pagination et de tri
     * @return une page de résultats {@link Page < Manga >} contenant les mangas
     */
    public Page<MangaBaseProjection> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all mangas by Pageable");
        return mangaDao.findAllMangas(pageable);
    }


    public MangaDto findMangaDtoById(Integer id) {
        LOGGER.info("Find manga by id");
        Manga manga = mangaDao.findManga2ById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manga with ID " + id + " not found"));
        return mangaMapper.mangaToMangaDto(manga);
    }



    public MangaProjection findMangaById(Integer id) {
        LOGGER.info("Find manga by id");
        return mangaDao.findMangaById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
    }

    @Transactional
    public MangaDto save(MangaDto mangaDto) {
        LOGGER.info("Save manga");
        LOGGER.info("manga mangaDto : {}", mangaDto);

        if (mangaDto.getPictures() == null || mangaDto.getPictures().isEmpty()) {
            LOGGER.error("Validation failed: A manga must have at least one image.");
            throw new IllegalArgumentException("A manga must contain at least one image.");
        }
        for (PictureLightDto pictureDto : mangaDto.getPictures()) {
            if (!pictureDao.existsById(pictureDto.getId())) {
                LOGGER.error("Validation failed: Image with ID {} does not exist.", pictureDto.getId());
                throw new IllegalArgumentException("Image with ID " + pictureDto.getId() + " does not exist.");
            }
        }

        Manga manga = mangaMapper.mangaToEntity(mangaDto);
        if (manga.getPriceHt() != null) {
            BigDecimal priceHt = manga.getPriceHt();
            BigDecimal multiplier = new BigDecimal("0.1");
            BigDecimal TVAmount = priceHt.multiply(multiplier);
            manga.setPriceHt(priceHt.add(TVAmount));
            LOGGER.info("Price adjusted with TVA (10%): {} → {}", priceHt, manga.getPriceHt());
        } else {
            LOGGER.warn("The price excluding TVA is NULL, no TVA calculation carried out!");
        }

        try {
            mangaDao.save(manga);
        } catch (Exception e) {
            LOGGER.error("Error saving manga", e);
            throw new RuntimeException("Error saving manga", e);
        }
        return mangaMapper.mangaToMangaDto(manga);
    }

    /**
     * Retrieve a list of four mangas
     * return a list of four mangas
     */
    public List<MangaDtoRandom> getFourMangaRandom() {
        List<MangaProjectionWithAuthor> projections = mangaDao.findFourMangasRandom();
        return projections.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
       
    }

    /**
     * Retrieve
     * return a list of four mangas
     */
    public List<MangaDtoRandom> getReleaseDateRaw() {
        List<MangaProjectionWithAuthor> projections = mangaDao.findMangasReleaseDateRaw();
        return projections.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    private MangaDtoRandom mapToDto(MangaProjectionWithAuthor projection) {
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
                            parts[2],
                            parts[1]);
                })
                .collect(Collectors.toSet());
    }


    /**
     * Retrieve a Random manga
     * return a list of one manga
     */
    public List<MangaDtoOne> getRandomManga() {
        List<Object[]> rawMangas = mangaDao.findRandomOneMangas();

        return rawMangas.stream()
                .map(this::mapToMangasRandom)
                .collect(Collectors.toList());
    }

    private MangaDtoOne mapToMangasRandom(Object[] row) {
        Integer idMangas = (Integer) row[0];
        String title = (String) row[1];
        String subtitle = (String) row[2];
        String summary = (String) row[3];
        BigDecimal price = (BigDecimal) row[4];
        String categoryString = (String) row[5];
        String genresString = (String) row[6];
        String authorsString = (String) row[7];
        String picture = (String) row[8];

        CategoryDto category = parseCategory(categoryString);
        Set<GenreDto> genres = parseGenre(genresString);
        Set<AuthorDtoRandom> authors = parseAuthors(authorsString);

        return new MangaDtoOne(idMangas, title, subtitle, summary, price, category, genres, authors, picture);
    }

    private CategoryDto parseCategory(String categoryString) {
        if (categoryString == null || categoryString.isEmpty()) {
            return null;
        }

        String[] parts = categoryString.split(":");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Catégorie mal formée : " + categoryString);
        }

        return new CategoryDto(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                parts[3]);
    }

    private Set<GenreDto> parseGenre(String genreString) {
        if (genreString == null || genreString.isEmpty()) {
            return Set.of();
        }

        return Arrays.stream(genreString.split("\\|"))
                .map(genreData -> {
                    String[] parts = genreData.split("@", -1);
                    if (parts.length < 3) {
                        throw new IllegalArgumentException("Genre mal formé : " + genreData);
                    }
                    return new GenreDto(parts[0], parts[1], parts[2]);
                })
                .collect(Collectors.toSet());
    }

    @Transactional
    public void deleteManga(Integer id) {
        LOGGER.info("Deleting author by id");
        Manga manga = mangaDao.findMangaId(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));

        manga.setIdCategories(null);
        for (Genre genre : manga.getGenres()) {
            genre.getMangas().remove(manga);
        }
        for (Author author : manga.getAuthors()) {
            author.getMangas().remove(manga);
        }
        manga.getGenres().clear();
        manga.getAuthors().clear();
        mangaDao.delete(manga);
    }

    @Transactional
    public MangaDto updateManga(Integer id, MangaDto mangaDto) {

        Manga manga = mangaDao.findMangaId(id)
                .orElseThrow(() -> new EntityNotFoundException("Manga with ID " + id + " not found"));

        manga.setTitle(mangaDto.getTitle());
        manga.setSummary(mangaDto.getSummary());
        manga.setPriceHt(mangaDto.getPriceHt());

        Integer categoryId = mangaDto.getIdCategories().getId();
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        manga.setIdCategories(category);
        Set<Genre> updatedGenres = new HashSet<>();

        if (mangaDto.getGenres() != null) {
            for (GenreLightDto dto : mangaDto.getGenres()) {
                Genre genre = genreDao.findById(dto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Genre with ID " + dto.getId() + " not found"));

                updatedGenres.add(genre);
            }
        }
        manga.setGenres(updatedGenres);

        Set<Author> updatedAuthors = mangaDto.getAuthors() == null ? new HashSet<>()
                : mangaDto.getAuthors().stream()
                        .map(dto -> authorDao.findById(dto.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Genre with ID " + dto.getId() + " not found")))
                        .collect(Collectors.toSet());
        manga.setAuthors(updatedAuthors);

        Set<Picture> updatedPictures = new HashSet<>();

        if (mangaDto.getPictures() != null) {
            for (PictureLightDto dto : mangaDto.getPictures()) {
                Picture picture = pictureDao.findById(dto.getId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Picture with ID " + dto.getId() + " not found"));

                picture.setIdMangas(manga);
                updatedPictures.add(picture);
            }
        }

        manga.getPictures().clear();
        manga.getPictures().addAll(updatedPictures);

        manga.getPictures().clear();
        manga.getPictures().addAll(updatedPictures);

        manga = mangaDao.save(manga);
        return mangaMapper.mangaToMangaDto(manga);
    }

    // public Page<MangaBaseProjection> getTest(Pageable pageable) {
    //     return mangaDao.findMangasWithMainPictures(pageable);
    // }

    public Page<MangaBaseProjection> getTitle(String letter,Pageable pageable) {
        return mangaDao.findMangaWithSearch(letter,pageable);
    }
    



    public Page<MangaDtoRandom> getPageMangas(Pageable pageable) {
        Page<MangaProjectionWithAuthor> projections = mangaDao.findMangasWithMainPicturesTest(pageable);
        return projections.map(this::mapToDto);
    }


 


}
