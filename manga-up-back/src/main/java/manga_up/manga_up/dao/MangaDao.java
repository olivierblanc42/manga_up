package manga_up.manga_up.dao;

import manga_up.manga_up.dto.MangaDto;
import manga_up.manga_up.dto.MangaDtoRandom;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.MangaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangaDao extends JpaRepository<Manga, Integer> {

    @Query("From Author ")
    Page<Manga> findMangasByPage(Pageable pageable);


    @Query("SELECT DISTINCT m FROM Manga m " +
            "LEFT JOIN FETCH  m.authors a " +
            "LEFT JOIN FETCH  m.genres g " +
            "LEFT JOIN FETCH  m.idCategories  c")
    Page<MangaProjection> findAllMangas(Pageable pageable);


    @Query( value = "SELECT m.Id_mangas, m.title,a.Id_authors, a.firstname, a.lastname,p.Id_picture,p.url " +
            "FROM manga m " +
            "JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas " +
            "JOIN author a ON ma.Id_authors = a.Id_authors  " +
            "JOIN picture p on p.Id_mangas = m.Id_mangas  " +
            "ORDER BY RAND() " +
            "LIMIT 4 " , nativeQuery = true)
    List<MangaDtoRandom> findRandomMangas();


    @Query( value = "SELECT m.Id_mangas, m.title,a.Id_authors, a.firstname, a.lastname,p.Id_picture,p.url " +
            "FROM manga m " +
            "JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas " +
            "JOIN author a ON ma.Id_authors = a.Id_authors  " +
            "JOIN picture p on p.Id_mangas = m.Id_mangas  " +
            "ORDER BY RAND() " +
            "LIMIT 1 " , nativeQuery = true)
    List<MangaDtoRandom> findRandomOneMangas();

}
