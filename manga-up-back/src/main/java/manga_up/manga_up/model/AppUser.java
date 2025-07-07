package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an application user within the system.
 * Includes user credentials, personal details, associations to address, gender,
 * comments, carts, favorite mangas, and more.
 */
@Entity
@Table(name = "app_user")
public class AppUser {

    /** Unique identifier for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_users", nullable = false)
    private Integer id;

    /** Unique username used for authentication. */
    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    /** User's first name. */
    @Size(max = 80)
    @NotNull
    @Column(name = "firstname", nullable = false, length = 80)
    private String firstname;

    /** User's last name. */
    @Size(max = 80)
    @NotNull
    @Column(name = "lastname", nullable = false, length = 80)
    private String lastname;

    /** Role assigned to the user (e.g., ADMIN, USER). */
    @Size(max = 10)
    @Column(name = "role", length = 10)
    private String role;


    @Size(max = 15)
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    /** Email address used for contact and authentication. */
    @Email(message = "Invalid email")
    @Size(max = 320)
    @NotNull
    @Column(name = "email", nullable = false, length = 320)
    private String email;


    @Size(max = 255)
    @Column(name = "avatar_url")
    private String url;



    /** Date and time when the user was created. */
    @Column(name = "created_at")
    private Instant createdAt;

    /** Hashed password for authentication. */
    @Size(min = 8, max = 128, message = "The password must be between 8 and 128 characters long.")
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    /** Address associated with the user. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_user_address", nullable = false)
    private UserAddress idUserAddress;

    /** Gender associated with the user. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_genders_user", nullable = false)
    private GenderUser idGendersUser;

    /** Shopping carts created by the user. */
    @OneToMany(mappedBy = "idUsers")
    private Set<Cart> carts = new LinkedHashSet<>();

    /** Comments posted by the user. */
    @OneToMany(mappedBy = "idUsers")
    private Set<Comment> comments = new LinkedHashSet<>();

    /** Profile picture of the user. */
    @OneToOne(mappedBy = "idUsers")
    private UserPicture userPicture;

    /** Favorite mangas of the user. */
    @ManyToMany
    @JoinTable(name = "appuser_manga", joinColumns = @JoinColumn(name = "Id_users"), inverseJoinColumns = @JoinColumn(name = "Id_mangas"))
    private Set<Manga> mangas = new LinkedHashSet<>();

    // --- Getters and setters below (no Javadoc necessary unless logic is added)
    // ---

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


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
