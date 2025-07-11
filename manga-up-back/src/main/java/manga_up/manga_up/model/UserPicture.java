package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "user_picture", schema = "manga_up", uniqueConstraints = {
        @UniqueConstraint(name = "user_picture_AK", columnNames = {"Id_users"})
})
public class UserPicture {
    @Id
    @Column(name = "Id_user_picture", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "url", length = 255)
    private String url;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_users", nullable = false)
    private AppUser idUsers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AppUser getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(AppUser idUsers) {
        this.idUsers = idUsers;
    }

}