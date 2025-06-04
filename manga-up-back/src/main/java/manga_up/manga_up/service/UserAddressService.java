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

/**
 * Service class to handle operations related to user addresses.
 */
@Service
public class UserAddressService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAddressService.class);

    private final AddressDao addressDao;
    private final UserAddressMapper userAddressMapper;

    public UserAddressService(AddressDao addressDao, UserAddressMapper userAddressMapper, UserDao userDao) {
        this.addressDao = addressDao;
        this.userAddressMapper = userAddressMapper;
    }

    /**
     * Retrieves a paginated list of user addresses.
     *
     * @param pageable a {@link Pageable} object containing pagination and sorting
     *                 information
     * @return a page of {@link UserAddressProjection} containing user addresses
     */
    public Page<UserAddressProjection> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all addresses by Pageable");
        return addressDao.findAllByPage(pageable);
    }

    /**
     * Saves a new user address.
     *
     * @param userAddressDto the DTO representing the user address to save
     * @return the saved user address as a DTO
     */
    @Transactional
    public UserAddressDto save(UserAddressDto userAddressDto) {
        LOGGER.info("Save address");
        LOGGER.info("userAddressDto: {}", userAddressDto);
        UserAddress userAddress = userAddressMapper.toEntity(userAddressDto);
        userAddress.setCreatedAt(Instant.now());
        try {
            addressDao.save(userAddress);
        } catch (Exception e) {
            LOGGER.error("Error saving user address", e);
            throw new RuntimeException("Error saving user address", e);
        }
        return userAddressMapper.toDto(userAddress);
    }

    /**
     * Finds a user address by its ID.
     *
     * @param id the ID of the user address
     * @return the user address projection
     * @throws EntityNotFoundException if the user address is not found
     */
    public UserAddressProjection findById(Integer id) {
        LOGGER.info("Find address by id");
        return addressDao.findUserAddressProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));
    }

    /**
     * Deletes a user address by its ID.
     *
     * @param id the ID of the user address to delete
     * @throws EntityNotFoundException if the address does not exist or is linked to
     *                                 a user
     */
    public void deleteUserAddress(Integer id) {
        LOGGER.info("Delete address by id");
        UserAddress userAddress = addressDao.findUserAddressById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address with id " + id + " not found"));
        if (!userAddress.getAppUsers().isEmpty()) {
            throw new EntityNotFoundException("The address is linked to a user and cannot be deleted");
        }
        addressDao.delete(userAddress);
    }

    /**
     * Updates an existing user address.
     *
     * @param userAddressId  the ID of the user address to update
     * @param userAddressDto the DTO containing updated information
     * @return the updated user address as a DTO
     * @throws RuntimeException if the user address is not found
     */
    @Transactional
    public UserAddressDto updateUserAddress(Integer userAddressId, UserAddressDto userAddressDto) {
        LOGGER.info("Update address");
        UserAddress userAddress = addressDao.findUserAddressById(userAddressId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        userAddress.setLine1(userAddressDto.getLine1());
        userAddress.setLine2(userAddressDto.getLine2());
        userAddress.setLine3(userAddressDto.getLine3());
        userAddress.setCity(userAddressDto.getCity());
        userAddress.setPostalCode(userAddressDto.getPostalCode());
        addressDao.save(userAddress);
        return userAddressMapper.toDto(userAddress);
    }
}
