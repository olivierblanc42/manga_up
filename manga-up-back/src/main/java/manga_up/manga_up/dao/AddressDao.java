package manga_up.manga_up.dao;

import manga_up.manga_up.dto.UserAddressDto;
import manga_up.manga_up.model.UserAddress;
import manga_up.manga_up.projection.UserAddressProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AddressDao extends JpaRepository<UserAddress, Integer> {



    @Query("SELECT ad FROM UserAddress ad LEFT JOIN FETCH ad.appUsers")
    Page<UserAddressProjection> findAllByPage(Pageable pageable);

    @Query("SELECT ad FROM UserAddress ad LEFT JOIN FETCH ad.appUsers WHERE ad.id = :id ")
    Optional<UserAddress> findUserAddressById(@Param("id") Integer id);


    @Query("SELECT ad FROM UserAddress ad LEFT JOIN FETCH ad.appUsers WHERE ad.id = :id ")
    Optional<UserAddressProjection> findUserAddressProjectionById(@Param("id") Integer id);

}
