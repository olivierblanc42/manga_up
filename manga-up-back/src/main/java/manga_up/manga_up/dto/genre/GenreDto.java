package manga_up.manga_up.dto.genre;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Genre}
 */
public class GenreDto implements Serializable {
    @NotNull
    private final Integer id;
    @NotNull
    @Size(max = 255, message = "URL must be at most 255 characters.")
    @Pattern(regexp = "^(https?://)?([\\w.-]+)+(:\\d+)?(/\\S*)?$", message = "URL is not valid.")
    private final String url;
    @NotNull
    @Size(max = 50)
    private final String label;
    @NotNull
    @Size(max = 3000, message = "Description must be at most 3000 characters.")
    private final String description;

    public GenreDto(Integer id, String url, String label, String description) {
        this.id = id;
        this.url = url;
        this.label = label;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GenreDto genreDto = (GenreDto) o;
        return Objects.equals(url, genreDto.url) &&
                Objects.equals(label, genreDto.label) &&
                Objects.equals(description, genreDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, label, description);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "url = " + url + ", " +
                "label = " + label + ", " +
                "description = " + description + ")";
    }
}