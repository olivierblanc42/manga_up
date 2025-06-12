package manga_up.manga_up.mongo.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.mongo.dao.ConnectionLogDao;
import manga_up.manga_up.mongo.model.ConnectionLog;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.UserAddressService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class LoginSuccessListenerTest {


    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private LoginSuccessListener loginSuccessListener;    

    @Mock
    private ConnectionLogDao connectionLogDao;
    @Mock
    private UserAddressService userAddressService;

    @Mock
    private JwtUtils jwtUtils;


    @Test
    
    void shouldtestOnApplicationEvent() {
        String username = "testuser";
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, "password");
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(auth);
     // Act
        loginSuccessListener.onApplicationEvent(event);

        // Assert
        ArgumentCaptor<ConnectionLog> logCaptor = ArgumentCaptor.forClass(ConnectionLog.class);
        verify(connectionLogDao).save(logCaptor.capture());

        ConnectionLog savedLog = logCaptor.getValue();
        assertThat(savedLog.getUsername()).isEqualTo(username);
        assertThat(savedLog.getLoginTime()).isBeforeOrEqualTo(LocalDateTime.now());
    }



}
