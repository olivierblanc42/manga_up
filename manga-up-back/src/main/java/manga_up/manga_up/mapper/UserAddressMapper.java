package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.UserAddressDto;
import manga_up.manga_up.model.UserAddress;
import org.mapstruct.Mapper;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Mapper(componentModel = "spring")
public class UserAddressMapper {

public UserAddressDto toDto(UserAddress userAddress) {
    return new UserAddressDto(
            userAddress.getLine1(),
            userAddress.getLine2(),
            userAddress.getLine3(),
            userAddress.getCity(),
            userAddress.getCreatedAt(),
            userAddress.getPostalCode()
    );
}


public UserAddress toEntity(UserAddressDto userAddressDto) {
    UserAddress  userAddress = new UserAddress();
    userAddress.setLine1(userAddressDto.getLine1());
    userAddress.setLine2(userAddressDto.getLine2());
    userAddress.setLine3(userAddressDto.getLine3());
    userAddress.setCity(userAddressDto.getCity());
    userAddress.setCreatedAt(Instant.now());
    userAddress.setPostalCode(userAddressDto.getPostalCode());
    return userAddress;
}


}
