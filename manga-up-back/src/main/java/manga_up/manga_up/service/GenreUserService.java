package manga_up.manga_up.service;

import manga_up.manga_up.dao.GenderUserDao;
import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dto.GenderUserDto;
import manga_up.manga_up.mapper.GenderUserMapper;
import manga_up.manga_up.model.GenderUser;
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
    private final GenderUserMapper genderUserMapper;

    public GenreUserService(GenderUserDao genderUserDao, GenderUserMapper genderUserMapper) {
        this.genderUserDao = genderUserDao;
        this.genderUserMapper = genderUserMapper;
    }


    public Page<GenderUserProjection> getGenreUsers(Pageable pageable) {
        LOGGER.info("Getting enderUser");
        return genderUserDao.getGenderUser(pageable);
    }


    public GenderUserDto saveGenreUser(GenderUserDto genreUserDto) {
        LOGGER.info("Saving genreUser");
        GenderUser genderUser = genderUserMapper.toEntity(genreUserDto) ;
           try {
               genderUserDao.save(genderUser);
           }catch (Exception e) {
               LOGGER.error("Error saving genre user", e);
               throw new RuntimeException("Error saving genre user", e);
           }
           return genderUserMapper.toDto(genderUser);
    }


    public GenderUserDto updateGenreUser( Integer genderId ,GenderUserDto genreUserDto) {
        LOGGER.info("Updating genreUser");
        GenderUser genderUser = genderUserDao.findGenderById(genderId).
                orElseThrow(() -> new RuntimeException("Genre with ID " + genderId + " not found"));

        genderUser.setLabel(genreUserDto.getLabel());
        genderUserDao.save(genderUser);
        return genderUserMapper.toDto(genderUser);
    }


}
