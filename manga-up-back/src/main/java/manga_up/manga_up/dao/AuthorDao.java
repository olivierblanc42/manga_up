package manga_up.manga_up.dao;

import manga_up.manga_up.model.Author;
import manga_up.manga_up.projection.author.AuthorProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Author} entities.
 * 
 */
@Repository
public interface AuthorDao extends JpaRepository<Author, Integer> {

    @Query("SELECT a FROM Author a")
    Page<AuthorProjection> findAllByPage(Pageable pageable);

    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.mangas WHERE a.id = :idAuthor")
    Optional<Author> findAuthorById(@Param("idAuthor") Integer idAuthor);

    @Query("SELECT a.id AS id, a.firstname AS firstname, a.lastname AS lastname, a.description AS description, " +
            "a.createdAt AS createdAt, a.birthdate AS birthdate, a.url AS url, a.genre AS genre " +
            "FROM Author a WHERE a.id = :idAuthor")
    Optional<AuthorProjection> findAuthorProjectionById(@Param("idAuthor") Integer idAuthor);

}
