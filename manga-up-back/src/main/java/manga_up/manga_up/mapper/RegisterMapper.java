package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.register.RegisterDto;
import manga_up.manga_up.model.AppUser;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
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
        appUser.setUsername(Jsoup.clean(registerDto.getUsername(), Safelist.none()));
        appUser.setFirstname(Jsoup.clean(registerDto.getFirstname(), Safelist.none()));
        appUser.setLastname(Jsoup.clean(registerDto.getLastname(), Safelist.none()));
        appUser.setRole(registerDto.getRole());
        appUser.setPhoneNumber(Jsoup.clean(registerDto.getPhoneNumber(), Safelist.none()));
        appUser.setEmail(registerDto.getEmail());
        appUser.setPassword(registerDto.getPassword());
        appUser.setIdUserAddress(userAddressMapper.toEntity(registerDto.getAddress()));
        appUser.setIdGendersUser(genderUserMapper.toEntity(registerDto.getGenderUserId()));
        return appUser;
    }


}
