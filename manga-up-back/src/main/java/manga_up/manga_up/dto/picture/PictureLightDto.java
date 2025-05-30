package manga_up.manga_up.dto.picture;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Picture}
 */
public class PictureLightDto implements Serializable {

    @NotNull
    @Min(value = 1, message = "Image ID must be greater than 0.")
    private final Integer id;
        @Size(max = 255)
    private final String url;
    private Boolean isMain;
    public PictureLightDto( Integer id,  String url , Boolean isMain) {
        this.id = id;
        this.url = url;
        this.isMain = isMain;
    }

    public @NotNull Integer getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public Boolean getIsMain() {
        return isMain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureLightDto entity = (PictureLightDto) o;
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