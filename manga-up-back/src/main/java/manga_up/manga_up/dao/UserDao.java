package manga_up.manga_up.dao;

import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.projection.appUser.AppUserProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);

    @Query("From AppUser ")
    Page<AppUser> findAllByPage(Pageable pageable);

     @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.idUserAddress WHERE u.username = :username")
     AppUserProjection findByUsernameProjection(@Param("username") String username);

     @Query("SELECT u FROM AppUser u " + 
     "LEFT JOIN FETCH u.idUserAddress "+ 
     "LEFT JOIN FETCH u.idGendersUser " +
     "LEFT JOIN FETCH u.mangas " +
     "WHERE u.username = :username ")
     AppUser findAppUserByUsername(@Param("username") String username);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.idUserAddress")
    Page<AppUserProjection> FindAllUser(Pageable pageable);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.idUserAddress ua WHERE ua = :userAddressId")
    List<AppUser> findUsersByAddressId(@Param("userAddressId") Integer userAddressId);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO appuser_manga (id_users, id_mangas) VALUES (:idUsers, :idMangas)", nativeQuery = true)
    void addUserInFavorite(@Param("idUsers") Integer idUsers, @Param("idMangas") Integer idMangas);   
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM appuser_manga WHERE id_users = :idUsers AND id_mangas = :idMangas", nativeQuery = true)
    void removeUserInFavorite(@Param("idUsers") Integer idUsers, @Param("idMangas") Integer idMangas);

    
    @Query(value = "SELECT COUNT(*) FROM appuser_manga WHERE id_users = :userId AND id_mangas = :mangaId", nativeQuery = true)
    int countFavorite(@Param("userId") Integer userId, @Param("mangaId") Integer mangaId);    

}
