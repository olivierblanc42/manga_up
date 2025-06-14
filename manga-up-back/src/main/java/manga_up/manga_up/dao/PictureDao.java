package manga_up.manga_up.dao;

import manga_up.manga_up.model.Picture;
import manga_up.manga_up.projection.pictureProjection.PictureProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Picture} entities.
 * 
 */
@Repository
public interface PictureDao extends JpaRepository<Picture, Integer> {

    List<Picture> findByIdMangasIdAndIsMainTrue(Integer mangaId);

    @Query("SELECT p FROM Picture p LEFT JOIN FETCH p.idMangas")
    Page<PictureProjection> findAllByPage(Pageable pageable);

    @Query("SELECT p FROM Picture p LEFT JOIN FETCH p.idMangas WHERE p.id = :id")
    Optional<Picture> findPictureById(@Param("id") Integer id);

    @Query("SELECT p FROM Picture p LEFT JOIN FETCH p.idMangas WHERE p.id = :id")
    Optional<PictureProjection> findPictureProjectionById(@Param("id") Integer id);

}
