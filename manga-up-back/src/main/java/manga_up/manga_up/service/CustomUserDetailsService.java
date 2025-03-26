package manga_up.manga_up.service;

import manga_up.manga_up.dao.AddressDao;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.RegisterDto;
import manga_up.manga_up.mapper.RegisterMapper;
import manga_up.manga_up.model.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER= LoggerFactory.getLogger(AddressService.class);


    private final UserDao userdao;
    private final RegisterMapper registerMapper;
    private final AddressDao addressDao;
    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userdao, RegisterMapper registerMapper, AddressDao addressDao, UserDao userDao) {
        this.userdao = userdao;
        this.registerMapper = registerMapper;
        this.addressDao = addressDao;
        this.userDao = userDao;
    }

    /**
     * Loads a user by their username.
     *
     * @param username The username to search for.
     * @return A {@link UserDetails} object containing user information.
     * @throws UsernameNotFoundException If the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userdao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),  user.getPassword() ,
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())));
    }



    public Page<AppUser> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all Users by Pageable");
        return userdao.findAllByPage(pageable);
    }


    @Transactional
    public RegisterDto saveUserDtoRegister(RegisterDto registerDto) {
        LOGGER.info("saveUserDtoRegister registerDTO : {}", registerDto);
        AppUser appUser = registerMapper.toAppUser(registerDto);
        LOGGER.info("saveUserDtoRegister user: {}", appUser);

        if (appUser.getIdUserAddress() != null && appUser.getIdUserAddress().getId() == null) {
            appUser.setIdUserAddress(addressDao.save(appUser.getIdUserAddress()));
        } else if (appUser.getIdUserAddress() != null) {
            appUser.setIdUserAddress(addressDao.findById(appUser.getIdUserAddress().getId()).orElse(null));
        }

        appUser.setIdGendersUser(appUser.getIdGendersUser());

        appUser.setCreatedAt(Instant.now());
        appUser.setRole("ROLE_USER");

        try {
            appUser = userDao.save(appUser);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la sauvegarde de l'utilisateur : ", e);
            throw new RuntimeException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }

        RegisterDto rDto = registerMapper.toDtoRegister(appUser);
        LOGGER.info("saveUserDtoRegister user : {}", rDto);

        return rDto;
    }



}
