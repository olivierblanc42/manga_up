package manga_up.manga_up.service;

import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.model.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER= LoggerFactory.getLogger(AddressService.class);


    private final UserDao userdao;

    public CustomUserDetailsService(UserDao userdao) {
        this.userdao = userdao;
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

}
