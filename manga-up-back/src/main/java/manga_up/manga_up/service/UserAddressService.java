package manga_up.manga_up.service;

import manga_up.manga_up.dao.AddressDao;

import manga_up.manga_up.dto.UserAddressDto;
import manga_up.manga_up.mapper.UserAddressMapper;
import manga_up.manga_up.model.UserAddress;
import manga_up.manga_up.projection.UserAddressProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;

@Service
public class UserAddressService {
    private static final Logger LOGGER= LoggerFactory.getLogger(UserAddressService.class);


    private final AddressDao addressDao;
    private final UserAddressMapper userAddressMapper;



    public UserAddressService(AddressDao addressDao, UserAddressMapper userAddressMapper) {
        this.addressDao = addressDao;
        this.userAddressMapper = userAddressMapper;
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



    public UserAddressDto save(UserAddressDto userAddressDto) {
        LOGGER.info("Save address");
        LOGGER.info("userAddressDto: {}", userAddressDto);
        UserAddress userAddress = userAddressMapper.toEntity(userAddressDto);
        userAddress.setCreatedAt(Instant.now());
        try{
            addressDao.save(userAddress);
        }catch (Exception e){
            LOGGER.error("Error saving user address",e);
            throw new RuntimeException("Error saving user address",e);
        }
        return userAddressMapper.toDto(userAddress);
    }


}
