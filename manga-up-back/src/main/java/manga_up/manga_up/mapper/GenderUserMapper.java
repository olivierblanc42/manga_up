package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.model.GenderUser;

import org.springframework.stereotype.Component;

@Component
public class GenderUserMapper {

    public static GenderUserDto toDto( GenderUser genderUser) {
        return new GenderUserDto(
                genderUser.getId(),
                genderUser.getLabel()
        );
    }


    public static GenderUser toEntity( GenderUserDto genderUserDto) {
        GenderUser genderUser = new GenderUser();
        genderUser.setId(genderUserDto.getId());
        genderUser.setLabel(genderUserDto.getLabel());
        return genderUser;
    }


}
