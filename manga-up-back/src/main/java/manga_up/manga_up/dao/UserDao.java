package manga_up.manga_up.dao;

import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.projection.AppUserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);

    @Query("From AppUser ")
    Page<AppUser> findAllByPage(Pageable pageable);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.idUserAddress")
    Page<AppUserProjection> FindAllUser(Pageable pageable);

}
