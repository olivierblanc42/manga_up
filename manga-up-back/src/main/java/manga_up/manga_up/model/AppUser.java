package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "app_user", schema = "manga_up", uniqueConstraints = {
        @UniqueConstraint(name = "app_user_AK", columnNames = {"username"})
})
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

    @Size(max = 15)
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Size(max = 320)
    @NotNull
    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Size(max = 128)
    @NotNull
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_adress", nullable = false)
    private Address idAdress;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_genders_user", nullable = false)
    private GenderUser idGendersUser;

}