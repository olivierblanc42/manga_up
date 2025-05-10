package manga_up.manga_up.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.appUser.UserProfilDto;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Picture;

@Component
public class AppUserMapper {

    private final MangaMapper mangaMapper;

    public AppUserMapper(MangaMapper mangaMapper) {
        this.mangaMapper = mangaMapper;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserMapper.class);

    public UserProfilDto toDtoAppUser(AppUser appUser) {
        LOGGER.info("AppUser size before mapping: {}", appUser.getUsername());

        return new UserProfilDto(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getFirstname(),
                appUser.getLastname(),
                appUser.getRole(),
                appUser.getPhoneNumber(),
                appUser.getEmail(),
                appUser.getCreatedAt(),
                UserAddressMapper.toDto(appUser.getIdUserAddress()),
                GenderUserMapper.toDto(appUser.getIdGendersUser()),
                mangaMapper.toMangaLightDtoSet(appUser.getMangas())
                );

    }



    public AppUser toEntityAppUser(UserProfilDto userProfilDto) {
        LOGGER.info("AppUser size before mapping: {}", userProfilDto.getUsername());

        AppUser appUser = new AppUser();
        appUser.setId(userProfilDto.getId());
        appUser.setUsername(userProfilDto.getUsername());
        appUser.setFirstname(userProfilDto.getFirstname());
        appUser.setLastname(userProfilDto.getLastname());
        appUser.setRole(userProfilDto.getRole());
        appUser.setPhoneNumber(userProfilDto.getPhoneNumber());
        appUser.setEmail(userProfilDto.getEmail());
        appUser.setCreatedAt(userProfilDto.getCreatedAt());
        appUser.setIdUserAddress(UserAddressMapper.toEntity(userProfilDto.getIdUserAddress()));
        appUser.setIdGendersUser(GenderUserMapper.toEntity(userProfilDto.getIdGendersUser()));
        appUser.setMangas(mangaMapper.toEntityMangas(userProfilDto.getMangas()));
        return appUser;
    }




}
