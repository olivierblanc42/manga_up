package manga_up.manga_up.dto.author;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Author}
 */
public class AuthorLigthDto implements Serializable {
    @NotNull
    private final Integer id;

    public AuthorLigthDto(Integer id) {
        this.id = id;

    }
    public Integer getId() {
        return id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorLigthDto entity = (AuthorLigthDto) o;
        return Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ")";
    }
}