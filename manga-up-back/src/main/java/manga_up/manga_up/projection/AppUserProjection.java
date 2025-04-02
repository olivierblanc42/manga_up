package manga_up.manga_up.projection;

public interface AppUserProjection {
    Integer getId();
    String getUsername();
    String getFirstname();
    String getLastname();
    String getEmail();
    String getPhoneNumber();
    UserAddressLittleProjection getIdUserAddress();
}
