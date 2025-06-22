package manga_up.manga_up.dao;

import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.genre.GenreProjection;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Genre} entities.
 * 
 */
@Repository
public interface GenreDao extends JpaRepository<Genre, Integer> {


    @Query(value = """
            SELECT
                g.Id_gender_mangas AS id,
                g.label AS label,
                g.url AS url,
                g.description AS description,
                g.created_at AS createdAt
            FROM genre g
            ORDER BY g.created_at DESC
            """, countQuery = "SELECT COUNT(*) FROM genre", nativeQuery = true)
    Page<GenreProjection> findAllByPage(Pageable pageable);    



    @Query(value = "SELECT genre.url, genre.label, genre.description " +
            "FROM genre ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<GenreDto> findRandomGenres();

    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.mangas WHERE g.id = :genreId ")
    Optional<Genre> findGenreById(@ParameterObject Integer genreId);

    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.mangas m LEFT JOIN FETCH m.pictures WHERE g.id = :genreId ")
    Optional<GenreProjection> findGenreProjectionById(@ParameterObject Integer genreId);

}
