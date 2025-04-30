package manga_up.manga_up.dao;

import manga_up.manga_up.model.Comment;
import manga_up.manga_up.projection.comment.CommentProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentDao extends JpaRepository<Comment, Integer> {

        @Query("SELECT co FROM Comment co " +
                        "LEFT JOIN FETCH co.idMangas " +
                        "LEFT JOIN FETCH co.idUsers ")
        Page<CommentProjection> findAllByPage(Pageable pageable);

        @Query("SELECT co FROM Comment co " +
                        "LEFT JOIN FETCH co.idMangas " +
                        "LEFT JOIN FETCH co.idUsers " +
                        "WHERE co.id = :id")
        Optional<Comment> findCommentById(@Param("id") Integer id);

        @Query("SELECT co FROM Comment co " +
                        "LEFT JOIN FETCH co.idMangas " +
                        "LEFT JOIN FETCH co.idUsers " +
                        "WHERE co.id = :id")
        Optional<CommentProjection> findCommentProjectionById(@Param("id") Integer id);

}
