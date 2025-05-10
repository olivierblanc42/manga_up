package manga_up.manga_up.dto.appUser;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.dto.manga.MangaLightDto;

/**
 * DTO for {@link manga_up.manga_up.model.AppUser}
 */
public class UserProfilDto implements Serializable {
    private Integer id;
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
    private String phoneNumber;
    @NotNull
    @Size(max = 320)
    private String email;
    private Instant createdAt;
    @NotNull
    private UserAddressDto idUserAddress;
    @NotNull
    private GenderUserDto idGendersUser;
    private Set<MangaLightDto> mangas;
    
    
 
    public UserProfilDto(Integer id, String username, String firstname, String lastname, String role, String phoneNumber, String email, Instant createdAt, UserAddressDto idUserAddress, GenderUserDto idGendersUser, Set<MangaLightDto> mangas) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdAt = createdAt;
        this.idUserAddress = idUserAddress;
        this.idGendersUser = idGendersUser;
        this.mangas = mangas;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserAddressDto getIdUserAddress() {
        return idUserAddress;
    }

    public void setIdUserAddress(UserAddressDto idUserAddress) {
        this.idUserAddress = idUserAddress;
    }

    public GenderUserDto getIdGendersUser() {
        return idGendersUser;
    }

    public void setIdGendersUser(GenderUserDto idGendersUser) {
        this.idGendersUser = idGendersUser;
    }


    public Set<MangaLightDto> getMangas() {
        return mangas;
    }

    public void setIdManga(Set<MangaLightDto> mangas) {
        this.mangas = mangas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfilDto entity = (UserProfilDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.firstname, entity.firstname) &&
                Objects.equals(this.lastname, entity.lastname) &&
                Objects.equals(this.role, entity.role) &&
                Objects.equals(this.phoneNumber, entity.phoneNumber) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.idUserAddress, entity.idUserAddress) &&
                Objects.equals(this.idGendersUser, entity.idGendersUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, lastname, role, phoneNumber, email, createdAt, idUserAddress, idGendersUser);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "username = " + username + ", " +
                "firstname = " + firstname + ", " +
                "lastname = " + lastname + ", " +
                "role = " + role + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "email = " + email + ", " +
                "createdAt = " + createdAt + ", " +
                "idUserAddress = " + idUserAddress + ", " +
                "idGendersUser = " + idGendersUser + ")";
    }
}
