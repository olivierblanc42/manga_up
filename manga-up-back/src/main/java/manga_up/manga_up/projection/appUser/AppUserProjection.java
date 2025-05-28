package manga_up.manga_up.projection.appUser;

import manga_up.manga_up.projection.userAdress.UserAddressLittleProjection;

public interface AppUserProjection {
    Integer getId();
    String getUsername();
    String getFirstname();
    String getLastname();
    String getEmail();
    String getPhoneNumber();
    UserAddressLittleProjection getIdUserAddress();
}
