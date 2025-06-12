package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.GenderUserDao;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.mapper.GenderUserMapper;
import manga_up.manga_up.model.GenderUser;
import manga_up.manga_up.projection.genderUser.GenderUserProjection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing GenderUser entities.
 * 
 * <p>
 * This service provides methods to perform CRUD operations on GenderUser,
 * including pagination, retrieval by ID, saving, updating, and deletion.
 * It also contains methods to fetch projections and DTO representations.
 * </p>
 */
@Service
public class GenreUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenreUserService.class);

    private final GenderUserDao genderUserDao;
    private final GenderUserMapper genderUserMapper;

    public GenreUserService(GenderUserDao genderUserDao, GenderUserMapper genderUserMapper) {
        this.genderUserDao = genderUserDao;
        this.genderUserMapper = genderUserMapper;
    }

    /**
     * Retrieves a paginated list of GenderUser projections.
     * 
     * @param pageable the pagination and sorting information
     * @return a {@link Page} of {@link GenderUserProjection}
     */
    public Page<GenderUserProjection> getGenreUsers(Pageable pageable) {
        LOGGER.info("Getting paginated GenderUser projections");
        return genderUserDao.getGenderUser(pageable);
    }

    /**
     * Retrieves a GenderUser projection by its ID.
     * 
     * @param id the ID of the GenderUser to retrieve
     * @return the {@link GenderUserProjection} corresponding to the given ID
     * @throws EntityNotFoundException if no GenderUser with the given ID exists
     */
    public GenderUserProjection getGenreUserById(Integer id) {
        LOGGER.info("Getting GenderUser projection by id: {}", id);
        return genderUserDao.findGenderUserProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gender user with id " + id + " not found"));
    }

    /**
     * Deletes a GenderUser entity by its ID.
     * 
     * <p>
     * Deletion is only allowed if the GenderUser is not linked to any AppUser.
     * Otherwise, an exception is thrown.
     * </p>
     * 
     * @param id the ID of the GenderUser to delete
     * @throws EntityNotFoundException if the GenderUser is not found or linked to
     *                                 AppUsers
     */
    @Transactional
    public void deleteGenreUserById(@PathVariable Integer id) {
        LOGGER.info("Deleting GenderUser with id: {}", id);
        GenderUser genderUser = genderUserDao.findGenderById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gender user with id " + id + " not found"));
        if (!genderUser.getAppUsers().isEmpty()) {
            throw new EntityNotFoundException("The Gender user is linked to a user and cannot be deleted");
        }
        genderUserDao.delete(genderUser);
    }

    /**
     * Saves a new GenderUser entity.
     * 
     * @param genreUserDto the DTO containing GenderUser data to save
     * @return the saved GenderUser mapped back as a DTO
     * @throws RuntimeException if an error occurs during saving
     */
    @Transactional
    public GenderUserDto saveGenreUser(GenderUserDto genreUserDto) {
        LOGGER.info("Saving new GenderUser");
        GenderUser genderUser = genderUserMapper.toEntity(genreUserDto);
        try {
            genderUserDao.save(genderUser);
        } catch (Exception e) {
            LOGGER.error("Error saving GenderUser", e);
            throw new RuntimeException("Error saving GenderUser", e);
        }
        return genderUserMapper.toDto(genderUser);
    }

    /**
     * Updates an existing GenderUser entity by ID.
     * 
     * @param genderId     the ID of the GenderUser to update
     * @param genreUserDto the DTO containing updated data
     * @return the updated GenderUser as a DTO
     * @throws RuntimeException if the GenderUser with given ID is not found
     */
    @Transactional
    public GenderUserDto updateGenreUser(Integer genderId, GenderUserDto genreUserDto) {
        LOGGER.info("Updating GenderUser with id: {}", genderId);
        GenderUser genderUser = genderUserDao.findGenderById(genderId)
                .orElseThrow(() -> new RuntimeException("Gender user with ID " + genderId + " not found"));
        genderUser.setLabel(genreUserDto.getLabel());
        genderUserDao.save(genderUser);
        return genderUserMapper.toDto(genderUser);
    }

    /**
     * Retrieves a list of all GenderUsers as DTOs.
     * 
     * @return a {@link List} of {@link GenderUserDto}
     */
    public List<GenderUserDto> getAllGenreUsers() {
        LOGGER.info("Getting all GenderUser DTOs");
        return genderUserDao.getGenderUserDto();
    }
}
