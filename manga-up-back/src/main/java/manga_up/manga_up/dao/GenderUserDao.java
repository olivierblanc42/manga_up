package manga_up.manga_up.dao;

import manga_up.manga_up.model.GenderUser;
import manga_up.manga_up.projection.GenderUserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderUserDao extends JpaRepository<GenderUser, Integer> {

   @Query("SELECT g FROM GenderUser g LEFT JOIN FETCH g.appUsers")
   Page<GenderUserProjection> getGenderUser(Pageable pageable);


   

}
