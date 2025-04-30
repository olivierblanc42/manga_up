package manga_up.manga_up.dao;

import manga_up.manga_up.model.Status;
import manga_up.manga_up.projection.status.StatusProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusDao extends JpaRepository<Status, Integer> {

    @Query("SELECT st FROM Status st ")
    Page<StatusProjection> findAllByPage(Pageable pageable);

}
