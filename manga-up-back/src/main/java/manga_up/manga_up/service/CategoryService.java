package manga_up.manga_up.service;

import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dto.CategoryDto;
import manga_up.manga_up.mapper.CategoryMapper;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.CategoryProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CategoryService {
    private static final Logger LOGGER= LoggerFactory.getLogger(CategoryService.class);

    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;
    public CategoryService(CategoryDao categoryDao, CategoryMapper categoryMapper) {
        this.categoryDao = categoryDao;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Récupère une page paginée de catégories.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Address >} contenant les catégories
     */
    public Page<CategoryProjection> FindAllCategorisByPage(Pageable pageable) {
        LOGGER.info("findAllByPage");
        return categoryDao.FindAllCategorisByPage(pageable);
    }


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

}
