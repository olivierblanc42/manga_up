package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_users", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 80)
    @NotNull
    @Column(name = "firstname", nullable = false, length = 80)
    private String firstname;

    @Size(max = 80)
    @NotNull
    @Column(name = "lastname", nullable = false, length = 80)
    private String lastname;

    @Size(max = 10)
    @Column(name = "role", length = 10)
    private String role;
    
    @Pattern(regexp = "^(\\+33|0)[1-9](\\d{2}){4}$", message = "Numéro de téléphone invalide")
    @Size(max = 15)
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Email(message = "Email invalide")
    @Size(max = 320)
    @NotNull
    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "created_at")
    private Instant createdAt;


    @Size(min = 6, max = 20, message = "Le mot de passe doit contenir entre 6 et 20 caractères")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{6,20}$", message = "Le mot de passe doit contenir au moins 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial")
    @NotNull
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_user_address", nullable = false)
    private UserAddress idUserAddress;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_genders_user", nullable = false)
    private GenderUser idGendersUser;

    @OneToMany(mappedBy = "idUsers")
    private Set<Cart> carts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUsers")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToOne(mappedBy = "idUsers")
    private UserPicture userPicture;

    @ManyToMany
    @JoinTable(name = "appuser_manga", joinColumns = @JoinColumn(name = "Id_users"), inverseJoinColumns = @JoinColumn(name = "Id_mangas"))
    private Set<Manga> mangas = new LinkedHashSet<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserAddress getIdUserAddress() {
        return idUserAddress;
    }

    public void setIdUserAddress(UserAddress idUserAddress) {
        this.idUserAddress = idUserAddress;
    }

    public GenderUser getIdGendersUser() {
        return idGendersUser;
    }

    public void setIdGendersUser(GenderUser idGendersUser) {
        this.idGendersUser = idGendersUser;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public UserPicture getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(UserPicture userPicture) {
        this.userPicture = userPicture;
    }

    public Set<Manga> getMangas() {
        return mangas;
    }

    public void setMangas(Set<Manga> mangas) {
        this.mangas = mangas;
    }
}