package manga_up.manga_up.service;

import manga_up.manga_up.dao.AddressDao;

import manga_up.manga_up.model.UserAddress;
import manga_up.manga_up.projection.UserAddressProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserAddressService {
    private static final Logger LOGGER= LoggerFactory.getLogger(UserAddressService.class);


    private final AddressDao addressDao;



    public UserAddressService(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    /**
     * Récupère une page paginée d'adresses.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page<UserAddress>} contenant les adresses
     */
    public Page<UserAddressProjection> findAllByPage(Pageable pageable) {
    LOGGER.info("Find all addresses by Pageable");
    return addressDao.findAllByPage(pageable);
    }

}
