package manga_up.manga_up.dao;

import manga_up.manga_up.dto.GenreDto;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.GenreProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreDao extends JpaRepository<Genre, Integer> {
    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.mangas")
    Page<GenreProjection> findAllByPage(Pageable pageable);
}
