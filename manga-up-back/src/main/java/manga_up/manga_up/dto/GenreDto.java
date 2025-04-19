package manga_up.manga_up.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import manga_up.manga_up.model.Manga;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link manga_up.manga_up.model.Genre}
 */
public class GenreDto implements Serializable {

    @NotNull
    private final Integer id;
    @NotNull
    @Size(max = 255)
    private String url;
    @NotNull
    @Size(max = 50)
    private final String label;

    public GenreDto(Integer id, String url, String label) {
        this.id = id;
        this.url = url;
        this.label = label;
    }
    public String getLabel() {
        return label;
    }


    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreDto entity = (GenreDto) o;
        return
                Objects.equals(this.label, entity.label) ;

    }

    @Override
    public int hashCode() {
        return Objects.hash( label);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "label = " + label + ", " ;
    }
}