package manga_up.manga_up.service;

import manga_up.manga_up.dao.GenderUserDao;
import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.projection.GenderUserProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GenreUserService {
    private static final Logger LOGGER= LoggerFactory.getLogger(GenreUserService.class);

    private final GenderUserDao genderUserDao;


    public GenreUserService(GenderUserDao genderUserDao) {
        this.genderUserDao = genderUserDao;
    }


    public Page<GenderUserProjection> getGenreUsers(Pageable pageable) {
        LOGGER.info("Getting enderUser");
        return genderUserDao.getGenderUser(pageable);
    }

}
