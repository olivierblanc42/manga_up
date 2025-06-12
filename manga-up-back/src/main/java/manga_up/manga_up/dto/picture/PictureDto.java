package manga_up.manga_up.dto.picture;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Picture}
 */
public class PictureDto implements Serializable {


    @Size(max = 255)
    private final String url;

    public PictureDto(  String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureDto entity = (PictureDto) o;
        return

                Objects.equals(this.url, entity.url) ;

    }

    @Override
    public int hashCode() {
        return Objects.hash( url);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +


                "url = " + url + ", " ;
    }
}