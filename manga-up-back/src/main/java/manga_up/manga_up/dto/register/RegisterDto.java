package manga_up.manga_up.dto.register;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.AppUser}
 */
public class RegisterDto implements Serializable {
    @NotNull
    @Size(max = 50)
    private String username;
    @NotNull
    @Size(max = 80)
    private String firstname;
    @NotNull
    @Size(max = 80)
    private String lastname;
    @Size(max = 10)
    private String role;
    @Size(max = 15)
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Numéro de téléphone invalide")
    private String phoneNumber;
    @NotNull
    @Pattern( regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 320)
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,20}$",
            message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    @Size(max = 128)
    private String password;
    private UserAddressDto address;
    private GenderUserDto genderUserId;

    public RegisterDto() {
    }

    public RegisterDto( String username, String firstname, String lastname, String role, String phoneNumber, String email, String password, UserAddressDto address,GenderUserDto genderUserId) {

        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.email = email;

        this.password = password;
        this.address = address;
        this.genderUserId = genderUserId;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GenderUserDto getGenderUserId() {
        return genderUserId;
    }

    public void setGenderUserId(GenderUserDto genderUser) {
        this.genderUserId = genderUser;
    }

    public UserAddressDto getAddress() {
        return address;
    }

    public void setAddress(UserAddressDto address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterDto entity = (RegisterDto) o;
        return
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.firstname, entity.firstname) &&
                Objects.equals(this.lastname, entity.lastname) &&
                Objects.equals(this.role, entity.role) &&
                Objects.equals(this.phoneNumber, entity.phoneNumber) &&
                Objects.equals(this.email, entity.email) &&

                Objects.equals(this.password, entity.password) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.genderUserId, entity.genderUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash( username, firstname, lastname, role, phoneNumber, email, password,genderUserId, address );
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +

                "username = " + username + ", " +
                "firstname = " + firstname + ", " +
                "lastname = " + lastname + ", " +
                "role = " + role + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "email = " + email;
    }
}