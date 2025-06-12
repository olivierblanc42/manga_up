package manga_up.manga_up.service;

import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryWithMangaResponse;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.mapper.CategoryMapper;
import manga_up.manga_up.mapper.MangaMapper;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.category.CategoryProjection;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;
    private final MangaDao mangaDao;
    private final MangaMapper mangaMapper;

    public CategoryService(CategoryDao categoryDao, CategoryMapper categoryMapper, MangaDao mangaDao, MangaMapper mangaMapper) {
        this.mangaDao = mangaDao;
        this.categoryDao = categoryDao;
        this.categoryMapper = categoryMapper;
        this.mangaMapper = mangaMapper;
    }

    /**
     * Retrieves a paginated list of categories.
     *
     * @param pageable the pagination and sorting information
     * @return a {@link Page} of {@link CategoryProjection} containing the
     *         categories
     */
    public Page<CategoryProjection> findAllCategoriesByPage(Pageable pageable) {
        LOGGER.info("findAllCategoriesByPage");
        return categoryDao.findAllCategoriesByPage(pageable);
    }

    /**
     * Finds a category by its id.
     *
     * @param id the id of the category
     * @return the {@link CategoryProjection} found
     * @throws EntityNotFoundException if no category found with the given id
     */
    public CategoryProjection findCategoryById(Integer id) {
        LOGGER.info("findCategoryById");
        return categoryDao.findCategoryProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
    }

    /**
     * Deletes a category by its id.
     * Throws an exception if the category is linked to mangas.
     *
     * @param id the id of the category to delete
     * @throws EntityNotFoundException if the category is linked to mangas or not
     *                                 found
     */
    public void deleteCategoryById(Integer id) {
        LOGGER.info("deleteCategoryById");
        Category category = categoryDao.findCategoryById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
        if (!category.getMangas().isEmpty()) {
            throw new EntityNotFoundException("The category is linked to mangas and cannot be deleted");
        }
        categoryDao.delete(category);
    }

    /**
     * Saves a new category.
     *
     * @param categoryDto the category DTO to save
     * @return the saved category DTO
     */
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        LOGGER.info("Saving category: {}", categoryDto);
        Category category = categoryMapper.toEntity(categoryDto);
        category.setCreatedAt(Instant.now());
        try {
            category = categoryDao.save(category);
        } catch (Exception e) {
            LOGGER.error("Error saving category: ", e);
            throw new RuntimeException("Error saving category", e);
        }
        return categoryMapper.toDtoCategory(category);
    }

    /**
     * Updates an existing category.
     *
     * @param id          the id of the category to update
     * @param categoryDto the category DTO with new data
     * @return the updated category DTO
     */
    @Transactional
    public CategoryDto update(Integer id, CategoryDto categoryDto) {
        Category category = categoryDao.findCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setLabel(categoryDto.getLabel());
        category.setDescription(categoryDto.getDescription());
        category.setUrl(categoryDto.getUrl());
        categoryDao.save(category);
        return categoryMapper.toDtoCategory(category);
    }

    /**
     * Retrieves a category with its associated mangas, paginated.
     *
     * @param categoryId the id of the category
     * @param pageable   pagination information
     * @return a {@link CategoryWithMangaResponse} containing the category and its
     *         mangas
     */
    public CategoryWithMangaResponse getCategoryWithMangas(Integer categoryId, Pageable pageable) {
        CategoryProjection category = categoryDao.findCategoryProjectionById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + categoryId + " not found"));
        Page<MangaDtoRandom> mangas = findMangasByCategory(categoryId, pageable);
        return new CategoryWithMangaResponse(category, mangas);
    }

    /**
     * Finds mangas by category.
     *
     * @param categoryId the category id
     * @param pageable   pagination information
     * @return a page of {@link MangaDtoRandom}
     */
    public Page<MangaDtoRandom> findMangasByCategory(Integer categoryId, Pageable pageable) {
        Page<MangaProjectionWithAuthor> projections = mangaDao.findMangasByCategory2(categoryId, pageable);
        return projections.map(mangaMapper::mapToDto);
    }


}
