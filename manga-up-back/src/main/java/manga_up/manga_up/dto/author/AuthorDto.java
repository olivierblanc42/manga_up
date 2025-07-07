package manga_up.manga_up.dto.author;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Author}
 */
public class AuthorDto implements Serializable {
    private final Integer id;
    @Size(max = 100)
    private final String lastname;
    @NotNull
    @Size(max = 50)
    private final String firstname;
    
    @NotNull
    private final String description;

    @NotNull
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\-,\\s]+$", message = "Genre contains invalid characters")
    private final String genre;
    @NotNull
    private final LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^(https?://).*$", message = "URL must be valid and start with http or https")
    private final String url;

    public AuthorDto(Integer id, String lastname, String firstname, String description, String genre, LocalDate birthdate,String url) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.description = description;
        this.genre = genre;
        this.birthdate = birthdate;
        this.url =url;
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


    public String getGenre() {
        return genre;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDto entity = (AuthorDto) o;
        return
                Objects.equals(this.lastname, entity.lastname) &&
                Objects.equals(this.firstname, entity.firstname) &&
                Objects.equals(this.description, entity.description);

    }

    @Override
    public int hashCode() {
        return Objects.hash( lastname, firstname, description);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "lastname = " + lastname + ", " +
                "firstname = " + firstname + ", " +
                "description = " + description + ", " ;

    }
}