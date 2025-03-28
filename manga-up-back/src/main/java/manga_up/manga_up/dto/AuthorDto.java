package manga_up.manga_up.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import manga_up.manga_up.model.Manga;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link manga_up.manga_up.model.Author}
 */
public class AuthorDto implements Serializable {
    private final Integer id;
    @NotNull
    @Size(max = 100)
    private final String lastname;
    @NotNull
    @Size(max = 50)
    private final String firstname;
    @NotNull
    private final String description;
    private final LocalDate createdAt;


    public AuthorDto(Integer id, String lastname, String firstname, String description, LocalDate createdAt) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.description = description;
        this.createdAt = createdAt;

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

    public String getDescription() {
        return description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDto entity = (AuthorDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.lastname, entity.lastname) &&
                Objects.equals(this.firstname, entity.firstname) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.createdAt, entity.createdAt) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastname, firstname, description, createdAt);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "lastname = " + lastname + ", " +
                "firstname = " + firstname + ", " +
                "description = " + description + ", " +
                "createdAt = " + createdAt + ", " ;

    }
}