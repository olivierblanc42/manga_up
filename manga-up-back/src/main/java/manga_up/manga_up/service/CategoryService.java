package manga_up.manga_up.service;

import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.CategoryProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private static final Logger LOGGER= LoggerFactory.getLogger(CategoryService.class);

    private final CategoryDao categoryDao;
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
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
}
