package manga_up.manga_up.mongo.controller;

import manga_up.manga_up.mongo.dao.ConnectionLogDao;
import manga_up.manga_up.mongo.model.ConnectionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ConnectionLogController {

    @Autowired
    private ConnectionLogDao connectionLogRepository;

    // Récupérer tous les logs de connexion
    @PreAuthorize("hasRole('ADMIN')")  
    @GetMapping
    public List<ConnectionLog> getAllLogs() {
        return connectionLogRepository.findAll();
    }
}
