package manga_up.manga_up.service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryWithMangaResponse;
import manga_up.manga_up.dto.genre.GenreWithMangasResponse;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.mapper.CategoryMapper;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.projection.genre.GenreProjection;
import manga_up.manga_up.projection.manga.MangaBaseProjection;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private static final Logger LOGGER= LoggerFactory.getLogger(CategoryService.class);

    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;
        private final MangaDao mangaDao;

    public CategoryService(CategoryDao categoryDao, CategoryMapper categoryMapper,MangaDao mangaDao) {
        this.mangaDao = mangaDao;
        this.categoryDao = categoryDao;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Récupère une page paginée de catégories.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Address >} contenant les catégories
     */
    public Page<CategoryProjection> findAllCategorisByPage(Pageable pageable) {
        LOGGER.info("findAllByPage");
        return categoryDao.findAllCategorisByPage(pageable);
    }


    public CategoryProjection findCategoryById(Integer id) {
        LOGGER.info("FindCategoryById");
        return categoryDao.findCategoryProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
    }

    public void deleteCategoryById(Integer id) {
        LOGGER.info("deleteCategoryById");
        Category category = categoryDao.findCategoryById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
        if(!category.getMangas().isEmpty()) {
            throw new EntityNotFoundException("The category is linked to many mangas it cannot be deleted");
        }
        categoryDao.delete(category);
    }

@Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        LOGGER.info("categoryDto : {}", categoryDto);
        Category category = categoryMapper.toEntity(categoryDto);
        LOGGER.info("category category : {}", category);
        category.setCreatedAt(Instant.now());
        try {
          category =  categoryDao.save(category);
        }catch (Exception e) {
            LOGGER.error("Error saving category: ", e);
            throw new RuntimeException("error saving category", e);
        }
        return categoryMapper.toDtoCategory(category);
    }


@Transactional
    public CategoryDto update( Integer id, CategoryDto categoryDto) {
       Category category = categoryDao.findCategoryById(id).
               orElseThrow(() -> new RuntimeException("category not found"));
       category.setLabel(categoryDto.getLabel());
       categoryDao.save(category);
       return categoryMapper.toDtoCategory(category);
    }


public CategoryWithMangaResponse getCategoryWithMangas(Integer categoryId, Pageable pageable) {
    // Récupération du genre
    CategoryProjection category = categoryDao.findCategoryProjectionById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + categoryId + " not found"));

    // Récupération des mangas avec auteurs parsés
    Page<MangaDtoRandom> mangas = findMangasByGenre(categoryId, pageable);

    // Construction de la réponse
    return new CategoryWithMangaResponse(category, mangas);
}

public Page<MangaDtoRandom> findMangasByGenre(Integer genreId, Pageable pageable) {
    Page<MangaProjectionWithAuthor> projections = mangaDao.findMangasByCategory2(genreId, pageable);
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
