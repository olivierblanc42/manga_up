package manga_up.manga_up.dao;

import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.category.CategoryProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Category} entities.
 * 
 */
@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category as c")
    Page<CategoryProjection> findAllCategoriesByPage(Pageable pageable);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.mangas WHERE c.id = :idCategory")
    Optional<Category> findCategoryById(@Param("idCategory") Integer idCategory);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.mangas m LEFT JOIN FETCH m.pictures WHERE  c.id = :idCategory")
    Optional<CategoryProjection> findCategoryProjectionById(@Param("idCategory") Integer idCategory);

}
