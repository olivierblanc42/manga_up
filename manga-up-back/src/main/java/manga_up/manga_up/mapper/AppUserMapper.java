package manga_up.manga_up.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import manga_up.manga_up.dto.appUser.UserProfilDto;
import manga_up.manga_up.model.AppUser;

@Component
public class AppUserMapper {

    private final UserAddressMapper userAddressMapper;

    private final GenderUserMapper genderUserMapper;

    private final MangaMapper mangaMapper;

    public AppUserMapper(MangaMapper mangaMapper, GenderUserMapper genderUserMapper, UserAddressMapper userAddressMapper) {
        this.mangaMapper = mangaMapper;
        this.genderUserMapper = genderUserMapper;
        this.userAddressMapper = userAddressMapper;
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
                userAddressMapper.toDto(appUser.getIdUserAddress()),
                genderUserMapper.toDto(appUser.getIdGendersUser()),
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
        appUser.setIdUserAddress(userAddressMapper.toEntity(userProfilDto.getIdUserAddress()));
        appUser.setIdGendersUser(genderUserMapper.toEntity(userProfilDto.getIdGendersUser()));
        appUser.setMangas(mangaMapper.toEntityMangas(userProfilDto.getMangas()));
        return appUser;
    }




}
