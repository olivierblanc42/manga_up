package manga_up.manga_up.dao;

import manga_up.manga_up.model.UserAddress;
import manga_up.manga_up.projection.UserAddressProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AddressDao extends JpaRepository<UserAddress, Integer> {

   // @Query("From UserAddress ")
   // Page<UserAddress> findAllByPage(Pageable pageable);


    @Query("SELECT ad FROM UserAddress ad LEFT JOIN FETCH ad.appUsers")
    Page<UserAddressProjection> findAllByPage(Pageable pageable);

}
