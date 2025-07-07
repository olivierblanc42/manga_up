package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.UserAdress.UserAdressDtoUpdate;
import manga_up.manga_up.model.UserAddress;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.mapstruct.Mapper;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Mapper(componentModel = "spring")
public class UserAddressMapper {

public  UserAddressDto toDto(UserAddress userAddress) {
    return new UserAddressDto(
            userAddress.getLine1(),
            userAddress.getLine2(),
            userAddress.getLine3(),
            userAddress.getCity(),
            userAddress.getCreatedAt(),
            userAddress.getPostalCode()
    );
}


public  UserAddress toEntity(UserAddressDto userAddressDto) {
    UserAddress  userAddress = new UserAddress();
    userAddress.setLine1( Jsoup.clean(userAddressDto.getLine1(), Safelist.none())  );
    userAddress.setLine2(Jsoup.clean(userAddressDto.getLine1(),Safelist.none()));
    userAddress.setLine3(Jsoup.clean(userAddressDto.getLine3(), Safelist.none() ));
    userAddress.setCity(userAddressDto.getCity());
    userAddress.setCreatedAt(Instant.now());
    userAddress.setPostalCode(Jsoup.clean(userAddressDto.getPostalCode(), Safelist.none()));
    return userAddress;
}

    public UserAdressDtoUpdate toDtoUserAdressDtoUpdate(UserAddress userAddress) {
        return new UserAdressDtoUpdate(
                userAddress.getLine1(),
                userAddress.getLine2(),
                userAddress.getLine3(),
                userAddress.getCity(),
                userAddress.getPostalCode());
    }

    public UserAddress toEntityUserAdressDtoUpdate(UserAdressDtoUpdate userAddressDto) {
        UserAddress userAddress = new UserAddress();
        userAddress.setLine1(Jsoup.clean(userAddressDto.getLine1(), Safelist.none()));
        userAddress.setLine2(Jsoup.clean(userAddressDto.getLine2(), Safelist.none()));
        userAddress.setLine3(Jsoup.clean(userAddressDto.getLine3(), Safelist.none()));
        userAddress.setCity(Jsoup.clean(userAddressDto.getCity(), Safelist.none()));
        userAddress.setPostalCode(Jsoup.clean(userAddressDto.getPostalCode(), Safelist.none()));
        return userAddress;
    }}
