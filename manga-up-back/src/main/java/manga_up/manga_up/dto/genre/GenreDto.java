package manga_up.manga_up.dto.genre;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Genre}
 */
public class GenreDto implements Serializable {


    @NotNull
    @Size(max = 255)
    private String url;
    @NotNull
    @Size(max = 50)
    private final String label;
    @NotNull
    private final String description;

    public GenreDto( String url, String label, String description) {
        this.url = url;
        this.label = label;
        this.description =description;
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