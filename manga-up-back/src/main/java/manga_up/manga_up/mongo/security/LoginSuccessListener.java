package manga_up.manga_up.mongo.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import manga_up.manga_up.mongo.dao.ConnectionLogDao;
import manga_up.manga_up.mongo.model.ConnectionLog;

import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private ConnectionLogDao connectionLogRepository;

   
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();

        ConnectionLog log = new ConnectionLog();
        log.setUsername(username);
        log.setLoginTime(LocalDateTime.now());

        connectionLogRepository.save(log);
    }
}
