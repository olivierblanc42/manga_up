package manga_up.manga_up.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Author}
 */
public class AuthorDtoRandom implements Serializable {
    private final Integer id;
    @NotNull
    @Size(max = 100)
    private final String lastname;
    @NotNull
    @Size(max = 50)
    private final String firstname;

    public AuthorDtoRandom(Integer id, String lastname, String firstname) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public Integer getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDtoRandom entity = (AuthorDtoRandom) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.lastname, entity.lastname) &&
                Objects.equals(this.firstname, entity.firstname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastname, firstname);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "lastname = " + lastname + ", " +
                "firstname = " + firstname + ")";
    }
}