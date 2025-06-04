package manga_up.manga_up.dao;

import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.manga.MangaBaseProjection;
import manga_up.manga_up.projection.manga.MangaProjection;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Manga} entities.
 * 
 */
@Repository
public interface MangaDao extends JpaRepository<Manga, Integer> {

    @Query("From Author ")
    Page<Manga> findMangasByPage(Pageable pageable);


    @Query("SELECT DISTINCT m FROM Manga m " +
            "LEFT JOIN FETCH m.authors a " +
            "LEFT JOIN FETCH m.genres g " +
            "LEFT JOIN FETCH m.idCategories c " +
            "LEFT JOIN FETCH m.pictures p " +
            "LEFT JOIN FETCH m.appUsers u " +
            "WHERE m.id = :idManga")
    Optional<Manga> findManga2ById(@Param("idManga") Integer idManga);

    @Query("SELECT DISTINCT m FROM Manga m " +
            "LEFT JOIN FETCH m.authors a " +
            "LEFT JOIN FETCH m.genres g " +
            "LEFT JOIN FETCH m.idCategories c " +
            "LEFT JOIN FETCH m.pictures p " +
            "WHERE m.id = :idManga")
    Optional<MangaProjection> findMangaById(@Param("idManga") Integer idManga);

    @Query("SELECT DISTINCT m FROM Manga m " +
            "LEFT JOIN FETCH  m.authors a " +
            "LEFT JOIN FETCH  m.genres g " +
            "LEFT JOIN FETCH  m.idCategories  c " +
            "LEFT JOIN FETCH   m.pictures p " +
            "WHERE m.id = :idManga ")
    Optional<Manga> findMangaId(@Param("idManga") Integer idManga);

    @Query("""
                SELECT
                  m.id AS id,
                  m.title AS title,
                  p.id AS pictureId,
                  p.url AS pictureUrl
                FROM Manga m
                LEFT JOIN m.pictures p
                WHERE p.isMain = true
            """)
    Page<MangaBaseProjection> findAllMangas(Pageable pageable);

    @Query(value = "SELECT " +
            "m.Id_mangas AS mangaId, " +
            "m.title, " +
            "GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname) SEPARATOR '|') AS authors, "
            +
            "p.url AS picture " +
            "FROM manga m " +
            "LEFT JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas " +
            "LEFT JOIN author a ON ma.Id_authors = a.Id_authors " +
            "LEFT JOIN picture p ON p.Id_mangas = m.Id_mangas " +
            "GROUP BY m.Id_mangas, m.title, p.Id_picture, p.url " +
            "ORDER BY m.release_date DESC " +
            "LIMIT 4", nativeQuery = true)
    List<MangaProjectionWithAuthor> findMangasReleaseDateRaw();

    @Query(value = "SELECT " +
            "m.Id_mangas AS mangaId, " +
            "m.title AS title, " +
            "GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname) ORDER BY a.Id_authors SEPARATOR '|') AS authors, "
            +
            "p.url AS picture " +
            "FROM manga m " +
            "LEFT JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas " +
            "LEFT JOIN author a ON ma.Id_authors = a.Id_authors " +
            "LEFT JOIN picture p ON p.Id_mangas = m.Id_mangas AND p.is_main = TRUE " +
            "GROUP BY m.Id_mangas, m.title, p.Id_picture, p.url " +
            "ORDER BY RAND() " +
            "LIMIT 4", nativeQuery = true)
    List<MangaProjectionWithAuthor> findFourMangasRandom();

    @Query(value = "SELECT " +
            "m.Id_mangas, " +
            "m.title, " +
            "m.subtitle, " +
            "m.summary, " +
            "m.price, " +
            "GROUP_CONCAT(DISTINCT CONCAT(c.Id_categories, ':', c.label, ':', c.description,':',c.url) SEPARATOR '|') AS categories, " +
            "GROUP_CONCAT(DISTINCT CONCAT(g.url, '@', g.label,'@', g.description) SEPARATOR '|') AS genres, "
            +
            "GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname) SEPARATOR '|') AS authors, "
            +
            "p.url AS pictures "
            +
            "FROM manga m " +
            "LEFT JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas " +
            "LEFT JOIN author a ON ma.Id_authors = a.Id_authors " +
            "LEFT JOIN picture p ON p.Id_mangas = m.Id_mangas " +
            "LEFT JOIN category c ON c.Id_categories = m.Id_categories " +
            "LEFT JOIN genres_manga gm ON gm.Id_mangas = m.Id_mangas " +
            "LEFT JOIN genre g ON g.Id_gender_mangas = gm.Id_gender_mangas " +
            "GROUP BY m.Id_mangas, m.title ,p.url " +
            "ORDER BY RAND() " +
            "LIMIT 1", nativeQuery = true)
    List<Object[]> findRandomOneMangas();

    @Query(value = """
            SELECT m.Id_mangas AS id,
                   m.title AS title,
                   p.Id_picture AS pictureId,
                   p.url AS pictureUrl,
                   GROUP_CONCAT(DISTINCT CONCAT(a.firstname, ':', a.lastname) SEPARATOR '|') AS authorFullName
            FROM manga m
            JOIN picture p ON m.Id_mangas = p.Id_mangas
            JOIN mangas_authors am ON am.Id_mangas = m.Id_mangas
            JOIN author a ON am.Id_authors = a.Id_authors
            WHERE p.is_main = TRUE
              AND am.Id_authors = :authorId
            GROUP BY m.Id_mangas, m.title, p.Id_picture, p.url
            """, countQuery = """
            SELECT COUNT(DISTINCT m.Id_mangas)
            FROM manga m
            JOIN picture p ON m.Id_mangas = p.Id_mangas
            JOIN mangas_authors am ON am.Id_mangas = m.Id_mangas
            WHERE p.is_main = TRUE
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
                          WHERE p.is_main = TRUE
                          AND  c.Id_categories = :categoryId
            """, countQuery = """
                SELECT COUNT(*)
                FROM manga m
                JOIN picture p ON m.Id_mangas = p.Id_mangas
                JOIN category c ON c.Id_categories = m.Id_categories
                WHERE p.is_main = TRUE
                  AND  c.Id_categories = :categoryId
            """, nativeQuery = true)
    Page<MangaBaseProjection> findMangasByCategory(@Param("categoryId") Integer categoryId, Pageable pageable);

    @Query(value = """
            SELECT m.Id_mangas AS id,
                         m.title AS title,
                         p.Id_picture AS pictureId,
                         p.url AS pictureUrl
                  FROM manga m
                  JOIN picture p ON m.Id_mangas = p.Id_mangas
                  JOIN genres_manga gm ON gm.Id_mangas = m.Id_mangas
                          WHERE p.is_main = TRUE
                          AND  gm.Id_gender_mangas = :genreId
            """, countQuery = """
                SELECT COUNT(*)
                FROM manga m
                  JOIN picture p ON m.Id_mangas = p.Id_mangas
                  JOIN genres_manga gm ON gm.Id_mangas = m.Id_mangas
                WHERE p.is_main = TRUE
                   AND  gm.Id_gender_mangas = :genreId
            """, nativeQuery = true)
    Page<MangaBaseProjection> findMangasByGenre(@Param("genreId") Integer genreId, Pageable pageable);




    @Query(value = """
            SELECT
                m.Id_mangas AS id,
                m.title AS title,
                p.Id_picture AS pictureId,
                p.url AS pictureUrl,
                GROUP_CONCAT(CONCAT(a.firstname, ' ', a.lastname) SEPARATOR ', ') AS authorFullName
            FROM manga m
            JOIN picture p ON m.Id_mangas = p.Id_mangas
            JOIN mangas_authors ma ON ma.id_mangas = m.Id_mangas
            JOIN author a ON a.id_authors = ma.id_authors
            WHERE p.is_main = TRUE AND LOWER(m.title) LIKE LOWER(CONCAT('%', :letter, '%'))
            GROUP BY m.Id_mangas, m.title, p.Id_picture, p.url
            ORDER BY m.title ASC
            """, countQuery = """
            SELECT COUNT(DISTINCT m.Id_mangas)
            FROM manga m
            JOIN picture p ON m.Id_mangas = p.Id_mangas
            JOIN mangas_authors ma ON ma.id_mangas = m.Id_mangas
            JOIN author a ON a.id_authors = ma.id_authors
            WHERE p.is_main = TRUE AND LOWER(m.title) LIKE LOWER(CONCAT('%', :letter, '%'))
            """, nativeQuery = true)
    Page<MangaBaseProjection> findByTitleWithGenres(@Param("letter") String letter, Pageable pageable);





    @Query(value = """
                    SELECT
                        m.Id_mangas AS mangaId,
                        m.title AS title,
                        GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname)
                            ORDER BY a.Id_authors SEPARATOR '|') AS authors,
                        p.url AS picture
                    FROM manga m
                    LEFT JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas
                    LEFT JOIN author a ON ma.Id_authors = a.Id_authors
                    LEFT JOIN picture p ON p.Id_mangas = m.Id_mangas AND p.is_main = TRUE
                    GROUP BY m.Id_mangas, m.title, p.Id_picture, p.url
                    """, countQuery = """
                    SELECT COUNT(DISTINCT m.Id_mangas)
                    FROM manga m
                    LEFT JOIN mangas_authors ma ON m.Id_mangas = ma.Id_mangas
                    LEFT JOIN picture p ON p.Id_mangas = m.Id_mangas AND p.is_main = 1
                    """, nativeQuery = true)
    Page<MangaProjectionWithAuthor> findMangasWithMainPicturesTest(Pageable pageable);    

    @Query(value = """
                    SELECT
                        m.Id_mangas AS mangaId,
                        m.title AS title,
                        GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname)
                            ORDER BY a.Id_authors SEPARATOR '|') AS authors,
                        p.url AS picture
                    FROM manga m
                    JOIN picture p ON m.Id_mangas = p.Id_mangas
                    JOIN mangas_authors ma ON ma.id_mangas = m.Id_mangas
                    JOIN author a ON ma.id_authors = a.id_authors
                    JOIN genres_manga gm ON gm.Id_mangas = m.Id_mangas
                    WHERE p.is_main = TRUE
                      AND gm.Id_gender_mangas = :genreId
                    GROUP BY m.Id_mangas, m.title, p.url
                    """, countQuery = """
                    SELECT COUNT(DISTINCT m.Id_mangas)
                    FROM manga m
                    JOIN picture p ON m.Id_mangas = p.Id_mangas
                    JOIN genres_manga gm ON gm.Id_mangas = m.Id_mangas
                    WHERE p.is_main = TRUE
                      AND gm.Id_gender_mangas = :genreId
                    """, nativeQuery = true)
    Page<MangaProjectionWithAuthor> findMangasByGenre2(@Param("genreId") Integer genreId, Pageable pageable);



    @Query(value = """
                    SELECT
                        m.Id_mangas AS mangaId,
                        m.title AS title,
                        GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname)
                            ORDER BY a.Id_authors SEPARATOR '|') AS authors,
                        p.url AS picture
                    FROM manga m
                    JOIN picture p ON m.Id_mangas = p.Id_mangas
                    JOIN mangas_authors am ON am.Id_mangas = m.Id_mangas
                    JOIN author a ON am.Id_authors = a.Id_authors
                    WHERE p.is_main = TRUE
                      AND am.Id_authors = :authorId
                    GROUP BY m.Id_mangas, m.title, p.url
                    """, countQuery = """
                    SELECT COUNT(DISTINCT m.Id_mangas)
                    FROM manga m
                    JOIN picture p ON m.Id_mangas = p.Id_mangas
                    JOIN mangas_authors am ON am.Id_mangas = m.Id_mangas
                    WHERE p.is_main = TRUE
                      AND am.Id_authors = :authorId
                    """, nativeQuery = true)
    Page<MangaProjectionWithAuthor> findMangasByAuthor2(@Param("authorId") Integer authorId, Pageable pageable);


    @Query(value = """
                    SELECT
                        m.Id_mangas AS mangaId,
                        m.title AS title,
                        GROUP_CONCAT(DISTINCT CONCAT(a.Id_authors, ':', a.firstname, ':', a.lastname)
                            ORDER BY a.Id_authors SEPARATOR '|') AS authors,
                        p.url AS picture
                    FROM manga m
                    JOIN picture p ON m.Id_mangas = p.Id_mangas
                    JOIN mangas_authors ma ON ma.id_mangas = m.Id_mangas
                    JOIN author a ON ma.id_authors = a.id_authors
                    JOIN category c ON c.Id_categories = m.Id_categories
                    WHERE p.is_main = TRUE
                      AND c.Id_categories = :categoryId
                    GROUP BY m.Id_mangas, m.title, p.url
                    """, countQuery = """
                    SELECT COUNT(DISTINCT m.Id_mangas)
                    FROM manga m
                    JOIN picture p ON m.Id_mangas = p.Id_mangas
                    JOIN category c ON c.Id_categories = m.Id_categories
                    WHERE p.is_main = TRUE
                      AND c.Id_categories = :categoryId
                    """, nativeQuery = true)
    Page<MangaProjectionWithAuthor> findMangasByCategory2(@Param("categoryId") Integer categoryId,
                    Pageable pageable);
    
}
