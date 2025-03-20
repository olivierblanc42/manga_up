package manga_up.manga_up.dao;

import manga_up.manga_up.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    @Query("From Category ")
    Page<Category> findAllByPage(Pageable pageable);
}
