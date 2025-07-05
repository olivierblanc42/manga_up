package manga_up.manga_up.dto.appUser;

import java.io.Serializable;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.UserAdress.UserAdressDtoUpdate;
import manga_up.manga_up.dto.genderUser.GenderUserDto;

public class UpdateUserDto implements Serializable {
    private Integer id;
    @NotNull

    @NotNull
    @Size(max = 80)
    private String firstname;
    @NotNull
    @Size(max = 80)
    private String lastname;


    @Size(max = 255)
    private String url;
    @Size(max = 15)
    private String phoneNumber;
    @NotNull
    @Size(max = 320)
    private String email;
    @NotNull
    private UserAdressDtoUpdate idUserAddress;
    @NotNull
    private GenderUserDto idGendersUser;

    public UpdateUserDto() {
        // constructeur par défaut requis par Jackson
    }
    
    public UpdateUserDto(Integer id, String firstname, String lastname, String phoneNumber,
            String email,
            String url,
            UserAdressDtoUpdate idUserAddress, GenderUserDto idGendersUser) {
        this.id = id;
     
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.url =url;
        this.idUserAddress = idUserAddress;
        this.idGendersUser = idGendersUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserAdressDtoUpdate getIdUserAddress() {
        return idUserAddress;
    }

    public void setIdUserAddress(UserAdressDtoUpdate idUserAddress) {
        this.idUserAddress = idUserAddress;
    }

    public GenderUserDto getIdGendersUser() {
        return idGendersUser;
    }

    public void setIdGendersUser(GenderUserDto idGendersUser) {
        this.idGendersUser = idGendersUser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UpdateUserDto entity = (UpdateUserDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.firstname, entity.firstname) &&
                Objects.equals(this.lastname, entity.lastname) &&
                Objects.equals(this.phoneNumber, entity.phoneNumber) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.idUserAddress, entity.idUserAddress) &&
                Objects.equals(this.idGendersUser, entity.idGendersUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,  firstname, lastname, phoneNumber, email, idUserAddress, idGendersUser);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "firstname = " + firstname + ", " +
                "lastname = " + lastname + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "email = " + email + ", " +
                "idUserAddress = " + idUserAddress + ", " +
                "idGendersUser = " + idGendersUser + ")";
    }

}
