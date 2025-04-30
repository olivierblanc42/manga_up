package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.appUser.UserResponseDto;
import manga_up.manga_up.model.AppUser;

import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    private final UserAddressMapper userAddressMapper;
    private final GenderUserMapper genderUserMapper;

    public UserResponseMapper(UserAddressMapper userAddressMapper, GenderUserMapper genderUserMapper) {
        this.userAddressMapper = userAddressMapper;
        this.genderUserMapper = genderUserMapper;
    }

    public UserResponseDto toDto(AppUser appUser) {
        return new UserResponseDto(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getFirstname(),
                appUser.getLastname(),
                appUser.getRole(),
                appUser.getPhoneNumber(),
                appUser.getEmail(),
                appUser.getCreatedAt(),
                userAddressMapper.toDto(appUser.getIdUserAddress()),
                genderUserMapper.toDto(appUser.getIdGendersUser())
        );
    }


    public AppUser toEntity(UserResponseDto userResponseDto) {
        AppUser appUser = new AppUser();
        appUser.setUsername(userResponseDto.getUsername());
        appUser.setFirstname(userResponseDto.getFirstname());
        appUser.setLastname(userResponseDto.getLastname());
        appUser.setRole(userResponseDto.getRole());
        appUser.setPhoneNumber(userResponseDto.getPhoneNumber());
        appUser.setEmail(userResponseDto.getEmail());
        appUser.setCreatedAt(userResponseDto.getCreatedAt());
        appUser.setIdUserAddress(userAddressMapper.toEntity(userResponseDto.getIdUserAddress()));
        appUser.setIdGendersUser(genderUserMapper.toEntity(userResponseDto.getIdGendersUser()));
        return appUser;
    }

}
