package manga_up.manga_up.dao;

import manga_up.manga_up.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorDao extends JpaRepository<Author, Integer> {

    @Query("From Author ")
    Page<Author> findAllByPage(Pageable pageable);
}
