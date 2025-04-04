package manga_up.manga_up.dao;

import manga_up.manga_up.dto.GenreDto;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.GenreProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreDao extends JpaRepository<Genre, Integer> {
    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.mangas")
    Page<GenreProjection> findAllByPage(Pageable pageable);



    @Query("SELECT g FROM Genre g  LEFT JOIN FETCH g.mangas " +
            "ORDER BY FUNCTION('RAND') ")
    List<GenreProjection> findRandomGenres(Pageable pageable);

}
