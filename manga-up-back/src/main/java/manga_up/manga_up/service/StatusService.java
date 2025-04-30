package manga_up.manga_up.service;

import manga_up.manga_up.dao.StatusDao;
import manga_up.manga_up.projection.status.StatusProjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class StatusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

    private final StatusDao statusDao;

    public StatusService(StatusDao statusDao) {
        this.statusDao = statusDao;
    }

    public Page<StatusProjection> findAllByPage(Pageable pageable) {
     LOGGER.info("Finding all Status");
     return statusDao.findAllByPage(pageable);
 }




}
