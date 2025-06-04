package manga_up.manga_up.service;

import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.appUser.UserProfilDto;
import manga_up.manga_up.mapper.AppUserMapper;
import manga_up.manga_up.mapper.UserResponseMapper;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.projection.appUser.AppUserProjection;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserDao userdao;
    private final AppUserMapper userMapper;

    public UserService(UserDao userdao, UserResponseMapper userResponseMapper, AppUserMapper userMapper) {
        this.userdao = userdao;
        this.userMapper = userMapper;
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param pageable Pagination and sorting information.
     * @return a page of {@link AppUserProjection} representing users.
     */
    public Page<AppUserProjection> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all users by Pageable");
        return userdao.FindAllUser(pageable);
    }

    /**
     * Retrieves the currently authenticated user's profile.
     *
     * @return a {@link ResponseEntity} containing the user's profile DTO or
     *         appropriate HTTP status.
     */
    public ResponseEntity<UserProfilDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            // Load the full user entity with related data
            AppUser appUser = userdao.findAppUserByUsername(username);

            if (appUser == null) {
                return ResponseEntity.notFound().build();
            }

            // Map entity to DTO
            UserProfilDto userDto = userMapper.toDtoAppUser(appUser);

            return ResponseEntity.ok(userDto);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Returns the authenticated {@link AppUser} entity.
     *
     * @return the authenticated user entity.
     * @throws AccessDeniedException if the user is not authenticated.
     */
    public AppUser getAuthenticatedUserEntity() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userdao.findAppUserByUsername(username);
        }

        throw new AccessDeniedException("User not authenticated");
    }

    /**
     * Adds a manga to the user's list of favorites.
     *
     * @param userId  the ID of the user.
     * @param mangaId the ID of the manga to add.
     */
    public void addFavorite(Integer userId, Integer mangaId) {
        userdao.addUserInFavorite(userId, mangaId);
    }

    /**
     * Removes a manga from the user's list of favorites.
     *
     * @param userId  the ID of the user.
     * @param mangaId the ID of the manga to remove.
     */
    public void removeFavorite(Integer userId, Integer mangaId) {
        userdao.removeUserInFavorite(userId, mangaId);
    }

    /**
     * Checks if a manga is in the user's favorites.
     *
     * @param userId  the ID of the user.
     * @param mangaId the ID of the manga to check.
     * @return {@code true} if the manga is a favorite, {@code false} otherwise.
     */
    public boolean isFavorite(Integer userId, Integer mangaId) {
        LOGGER.info("[SERVICE] Checking if manga ID={} is favorite for user ID={}", mangaId, userId);
        int count = userdao.countFavorite(userId, mangaId);
        LOGGER.info("[SERVICE] COUNT result = {}", count);
        return count > 0;
    }
}
