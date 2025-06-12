package manga_up.manga_up.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import manga_up.manga_up.dao.AddressDao;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.register.RegisterDto;
import manga_up.manga_up.mapper.RegisterMapper;
import manga_up.manga_up.model.AppUser;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private RegisterMapper registerMapper;

    @Mock
    private AddressDao addressDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldLoadUserByUsername() {
        AppUser user = new AppUser();
        user.setUsername("john");
        user.setPassword("pass");
        user.setRole("ROLE_USER");

        when(userDao.findByUsername("john")).thenReturn(user);

        var result = service.loadUserByUsername("john");

        assertEquals("john", result.getUsername());
        assertEquals("pass", result.getPassword());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userDao.findByUsername("unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("unknown"));
    }

  @Test
    void shouldSaveUserDtoRegister() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("john");
        dto.setPassword("rawpass");

        AppUser mappedUser = new AppUser();
        mappedUser.setUsername("john");

        when(passwordEncoder.encode("rawpass")).thenReturn("encoded");
        when(registerMapper.toAppUser(dto)).thenReturn(mappedUser);
        when(userDao.save(any(AppUser.class))).thenReturn(mappedUser);
        when(registerMapper.toDtoRegister(mappedUser)).thenReturn(dto);

        RegisterDto result = service.saveUserDtoRegister(dto);

        assertEquals("john", result.getUsername());
        verify(passwordEncoder).encode("rawpass");
        verify(userDao).save(any(AppUser.class));
    }
}
