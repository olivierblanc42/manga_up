package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.AddressDao;

import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.mapper.UserAddressMapper;
import manga_up.manga_up.model.UserAddress;
import manga_up.manga_up.projection.userAdress.UserAddressProjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserAddressService {
    private static final Logger LOGGER= LoggerFactory.getLogger(UserAddressService.class);


    private final AddressDao addressDao;
    private final UserAddressMapper userAddressMapper;


    public UserAddressService(AddressDao addressDao, UserAddressMapper userAddressMapper, UserDao userDao) {
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


@Transactional
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

    public UserAddressProjection findById(Integer id) {
       LOGGER.info("Find address by id");
       return addressDao.findUserAddressProjectionById(id)
               .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));
    }


   public void  deleteUserAddress(Integer id) {
        LOGGER.info("Delete address by id");
      UserAddress userAddress = addressDao.findUserAddressById(id)
              .orElseThrow(() -> new EntityNotFoundException("Address with id " + id + " not found"));
      if(!userAddress.getAppUsers().isEmpty()) {
        throw new EntityNotFoundException("The address is linked to a user it cannot be deleted");
      }
      addressDao.delete(userAddress);
   }


@Transactional
 public UserAddressDto updateUserAddress(Integer userAddressId, UserAddressDto userAddressDto) {
        LOGGER.info("Update address");
     UserAddress userAddress = addressDao.findUserAddressById(userAddressId).
             orElseThrow(() -> new RuntimeException("Author not found"));
     userAddress.setLine1(userAddressDto.getLine1());
     userAddress.setLine2(userAddressDto.getLine2());
     userAddress.setLine3(userAddressDto.getLine3());
     userAddress.setCity(userAddressDto.getCity());
     userAddress.setPostalCode(userAddressDto.getPostalCode());
     addressDao.save(userAddress);
     return userAddressMapper.toDto(userAddress);
 }

}
