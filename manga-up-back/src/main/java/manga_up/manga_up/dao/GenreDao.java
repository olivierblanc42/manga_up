package manga_up.manga_up.dao;

import manga_up.manga_up.dto.GenreDto;
import manga_up.manga_up.dto.GenreLightDto;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.GenreProjection;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreDao extends JpaRepository<Genre, Integer> {
    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.mangas")
    Page<GenreProjection> findAllByPage(Pageable pageable);



    @Query( value = "SELECT  genre.Id_gender_mangas ,genre.url, genre.label " +
    "FROM genre  " +
    "ORDER BY RAND() " +
    "LIMIT 4 " , nativeQuery = true)
    List<GenreDto> findRandomGenres();



    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.mangas WHERE g.id = :genreId ")
    Optional<Genre> findGenreById(@ParameterObject Integer genreId);

    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.mangas WHERE g.id = :genreId ")
    Optional<GenreProjection> findGenreProjectionById(@ParameterObject Integer genreId);



}
