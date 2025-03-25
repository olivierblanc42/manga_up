package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.GenderUserDto;
import manga_up.manga_up.model.GenderUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GenderUserMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(GenderUserMapper.class);

    public GenderUserDto toDto( GenderUser genderUser) {
        return new GenderUserDto(
                genderUser.getId(),
                genderUser.getLabel()
        );
    }


    public GenderUser toEntity( GenderUserDto genderUserDto) {
        GenderUser genderUser = new GenderUser();
        genderUser.setId(genderUserDto.getId());
        genderUser.setLabel(genderUserDto.getLabel());
        return genderUser;
    }


}
