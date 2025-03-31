package manga_up.manga_up.dao;

import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.MangaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MangaDao extends JpaRepository<Manga, Integer> {

    @Query("From Author ")
    Page<Manga> findMangasByPage(Pageable pageable);


    @Query("SELECT DISTINCT m FROM Manga m " +
            "JOIN m.authors a " +
            "JOIN m.genres g " +
            "JOIN m.idCategories  c")
    Page<MangaProjection> findAllMangas(Pageable pageable);
}
