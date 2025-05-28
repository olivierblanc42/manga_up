package manga_up.manga_up.dto.appUser;

import java.io.Serializable;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


/**
 * DTO for {@link manga_up.manga_up.model.AppUser}
 */
public class UserFavoriteDto implements Serializable {
    private Integer id;
    @NotNull
    @Size(max = 50)
    private String username;


    public  UserFavoriteDto(Integer id, String username) {
        this.id = id;
        this.username = username;
    
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFavoriteDto entity = (UserFavoriteDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.username, entity.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "username = " + username + ", "; 
    }
}
