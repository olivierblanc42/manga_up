package manga_up.manga_up.service;

import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.appUser.UserProfilDto;
import manga_up.manga_up.mapper.AppUserMapper;
import manga_up.manga_up.mapper.UserResponseMapper;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.projection.appUser.AppUserProjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER= LoggerFactory.getLogger(UserAddressService.class);

    private final UserDao userdao;
    private final AppUserMapper userMapper;
   

    public UserService(UserDao userdao, UserResponseMapper userResponseMapper, AppUserMapper userMapper) {
        this.userdao = userdao;
        this.userMapper = userMapper;
    
    }


    /**
     * Récupère une page paginée d'adresses.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Address >} contenant les adresses
     */
    public Page<AppUserProjection> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all users by Pageable");
       return userdao.FindAllUser(pageable);
    }

public ResponseEntity<UserProfilDto> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
        String username = authentication.getName();

        // Charger l'utilisateur complet (avec ses relations)
        AppUser appUser = userdao.findAppUserByUsername(username);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Mapper l'entité en DTO
        UserProfilDto userDto = userMapper.toDtoAppUser(appUser);

        return ResponseEntity.ok(userDto);
    }

    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}

}
