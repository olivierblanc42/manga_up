package manga_up.manga_up.dto.picture;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Picture}
 */
public class PictureDtoRandom implements Serializable {
    private final Integer id;
    @Size(max = 255)
    private final String url;

    public PictureDtoRandom(Integer id, String url) {
        this.id = id;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureDtoRandom entity = (PictureDtoRandom) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.url, entity.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "url = " + url + ")";
    }
}