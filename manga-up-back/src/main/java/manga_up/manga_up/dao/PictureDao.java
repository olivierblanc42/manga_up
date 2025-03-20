package manga_up.manga_up.dao;

import manga_up.manga_up.model.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureDao extends JpaRepository<Picture, Integer> {

    @Query("From Picture ")
    Page<Picture> findAllByPage(Pageable pageable);
}
