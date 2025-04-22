package manga_up.manga_up.dao;

import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.MangaBaseProjection;
import manga_up.manga_up.projection.MangaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MangaDao extends JpaRepository<Manga, Integer> {

        @Query("From Author ")
        Page<Manga> findMangasByPage(Pageable pageable);

        @Query(value = """
                            SELECT m.Id_mangas AS id, m.title AS title, p.Id_picture AS pictureId, p.url AS pictureUrl
                            FROM manga m
                            JOIN picture p ON m.Id_mangas = p.Id_mangas
                            WHERE p.is_main = 1
                        """, nativeQuery = true)
        Page<MangaBaseProjection> findMangasWithMainPictures(Pageable pageable);

        @Query("SELECT DISTINCT m FROM Manga m " +
                        "LEFT JOIN FETCH  m.authors a " +
                        "LEFT JOIN FETCH  m.genres g " +
                        "LEFT JOIN FETCH  m.idCategories  c " +
                        "WHERE m.id = :idManga ")
        Optional<MangaProjection> findMangaById(@Param("idManga") Integer idManga);

        @Query("SELECT DISTINCT m FROM Manga m " +
                        "LEFT JOIN FETCH  m.authors a " +
                        "LEFT JOIN FETCH  m.genres g " +
                        "LEFT JOIN FETCH  m.idCategories  c " +
                        "LEFT JOIN FETCH   m.pictures p " +
                        "WHERE m.id = :idManga ")
        Optional<Manga> findMangaId(@Param("idManga") Integer idManga);

        @Query("SELECT DISTINCT m FROM Manga m " +
                        "LEFT JOIN FETCH  m.authors a " +
                        "LEFT JOIN FETCH  m.genres g " +
                        "LEFT JOIN FETCH  m.idCategories  c")
        Page<MangaProjection> findAllMangas(Pageable pageable);

        @Query(value = "SELECT " +
                        "m.Id_mangas, " +
                        "m.title, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname) SEPARATOR '|') AS authors, "
                        +
                        "GROUP_CONCAT(DISTINCT CASE WHEN p.is_main = 1 THEN CONCAT(p.Id_picture, '@', p.url) ELSE NULL END SEPARATOR '|') AS pictures "
                        +
                        "FROM manga m " +
                        "LEFT JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas " +
                        "LEFT JOIN author a ON ma.Id_authors = a.Id_authors " +
                        "LEFT JOIN picture p ON p.Id_mangas = m.Id_mangas " +
                        "GROUP BY m.Id_mangas, m.title " +
                        "ORDER BY m.release_date DESC " +
                        "LIMIT 4", nativeQuery = true)
        List<Object[]> findMangasReleaseDateRaw();

        @Query(value = "SELECT " +
                        "m.Id_mangas, " +
                        "m.title, " +
                        "m.subtitle, " +
                        "m.summary, " +
                        "m.price, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(c.Id_categories, ':', c.label) SEPARATOR '|') AS categories, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(g.Id_gender_mangas, '@' ,g.url, '@', g.label) SEPARATOR '|') AS genres, "
                        +
                        "GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname) SEPARATOR '|') AS authors, "
                        +
                        "GROUP_CONCAT(DISTINCT CASE WHEN p.is_main = 1 THEN CONCAT(p.Id_picture, '@', p.url) ELSE NULL END SEPARATOR '|') AS pictures "
                        +
                        "FROM manga m " +
                        "LEFT JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas " +
                        "LEFT JOIN author a ON ma.Id_authors = a.Id_authors " +
                        "LEFT JOIN picture p ON p.Id_mangas = m.Id_mangas " +
                        "LEFT JOIN category c ON c.Id_categories = m.Id_categories " +
                        "LEFT JOIN genres_manga gm ON gm.Id_mangas = m.Id_mangas " +
                        "LEFT JOIN genre g ON g.Id_gender_mangas = gm.Id_gender_mangas " +
                        "GROUP BY m.Id_mangas, m.title " +
                        "ORDER BY RAND() " +
                        "LIMIT 1", nativeQuery = true)
        List<Object[]> findRandomOneMangas();





        @Query(value = """
                SELECT m.Id_mangas AS id,
                       m.title AS title,
                       p.Id_picture AS pictureId,
                       p.url AS pictureUrl
                FROM manga m
                JOIN picture p ON m.Id_mangas = p.Id_mangas
                JOIN mangas_authors am ON am.Id_mangas = m.Id_mangas
                WHERE p.is_main = 1
                  AND am.Id_authors= :authorId
            """, countQuery = """
                SELECT COUNT(*)
                FROM manga m
                JOIN picture p ON m.Id_mangas = p.Id_mangas
                JOIN mangas_authors am ON am.Id_mangas = m.Id_mangas
                WHERE p.is_main = 1
                  AND am.Id_authors = :authorId
            """, nativeQuery = true)
        Page<MangaBaseProjection> findMangasByAuthor(@Param("authorId") Integer authorId, Pageable pageable);




        @Query(value = """
            SELECT m.Id_mangas AS id,
                                 m.title AS title,
                                 p.Id_picture AS pictureId,
                                 p.url AS pictureUrl
                          FROM manga m
                          JOIN picture p ON m.Id_mangas = p.Id_mangas
                          JOIN category c ON c.Id_categories = m.Id_categories
                          WHERE p.is_main = 1
                          AND  c.Id_categories = :categoryId
            """, countQuery = """
                SELECT COUNT(*)
                FROM manga m
                JOIN picture p ON m.Id_mangas = p.Id_mangas
                JOIN mangas_authors am ON am.Id_mangas = m.Id_mangas
                WHERE p.is_main = 1
                  AND  c.Id_categories = :categoryId
            """, nativeQuery = true)
        Page<MangaBaseProjection> findMangasByCategory(@Param("categoryId") Integer categoryId, Pageable pageable);



}
