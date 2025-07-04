package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.model.GenderUser;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class GenderUserMapper {

    public  GenderUserDto toDto( GenderUser genderUser) {
        return new GenderUserDto(
                genderUser.getId(),
                genderUser.getLabel()
        );
    }


    public GenderUser toEntity( GenderUserDto genderUserDto) {
        GenderUser genderUser = new GenderUser();
        genderUser.setId(genderUserDto.getId());
        genderUser.setLabel(Jsoup.clean(genderUserDto.getLabel(),Safelist.none()));
        return genderUser;
    }

}
