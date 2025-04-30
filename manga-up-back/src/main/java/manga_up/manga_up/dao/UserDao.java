package manga_up.manga_up.dao;

import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.projection.appUser.AppUserProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);

    @Query("From AppUser ")
    Page<AppUser> findAllByPage(Pageable pageable);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.idUserAddress")
    Page<AppUserProjection> FindAllUser(Pageable pageable);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.idUserAddress ua WHERE ua = :userAddressId")
    List<AppUser> findUsersByAddressId(@Param("userAddressId") Integer userAddressId);
}
