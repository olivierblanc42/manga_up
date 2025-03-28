package manga_up.manga_up.dao;

import manga_up.manga_up.model.Author;
import manga_up.manga_up.projection.AuthorProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorDao extends JpaRepository<Author, Integer> {

    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.mangas")
    Page<AuthorProjection> findAllByPage(Pageable pageable);
}
