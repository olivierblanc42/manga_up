package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.register.RegisterDto;
import manga_up.manga_up.model.AppUser;

import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    private final UserAddressMapper userAddressMapper;
    private final GenderUserMapper genderUserMapper;

    public RegisterMapper(UserAddressMapper userAddressMapper, GenderUserMapper genderUserMapper) {
        this.userAddressMapper = userAddressMapper;
        this.genderUserMapper = genderUserMapper;
    }


    public RegisterDto toDtoRegister(AppUser appUser) {
        return new RegisterDto(
                appUser.getUsername(),
                appUser.getFirstname(),
                appUser.getLastname(),
                appUser.getRole(),
                appUser.getPhoneNumber(),
                appUser.getEmail(),
                appUser.getPassword(),
                userAddressMapper.toDto(appUser.getIdUserAddress()),
                genderUserMapper.toDto(appUser.getIdGendersUser())

        );
    }

    public AppUser toAppUser(RegisterDto registerDto) {
        AppUser appUser = new AppUser();
        appUser.setUsername(registerDto.getUsername());
        appUser.setFirstname(registerDto.getFirstname());
        appUser.setLastname(registerDto.getLastname());
        appUser.setRole(registerDto.getRole());
        appUser.setPhoneNumber(registerDto.getPhoneNumber());
        appUser.setEmail(registerDto.getEmail());
        appUser.setPassword(registerDto.getPassword());
        appUser.setIdUserAddress(userAddressMapper.toEntity(registerDto.getAddress()));
        appUser.setIdGendersUser(genderUserMapper.toEntity(registerDto.getGenderUserId()));
        return appUser;
    }


}
