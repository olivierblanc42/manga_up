package manga_up.manga_up.service;

import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.UserResponseDto;
import manga_up.manga_up.mapper.UserResponseMapper;
import manga_up.manga_up.model.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER= LoggerFactory.getLogger(AddressService.class);

    private final UserDao userdao;
    private final UserResponseMapper userResponseMapper;

    public UserService(UserDao userdao, UserResponseMapper userResponseMapper) {
        this.userdao = userdao;
        this.userResponseMapper = userResponseMapper;
    }


    /**
     * Récupère une page paginée d'adresses.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Address >} contenant les adresses
     */
    public Page<UserResponseDto> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all users by Pageable");
        Page<AppUser> users = userdao.findAll(pageable);
        return users.map(userResponseMapper::toDto);
    }

}
