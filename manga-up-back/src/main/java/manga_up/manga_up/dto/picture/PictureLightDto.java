package manga_up.manga_up.dto.picture;

import jakarta.validation.constraints.Min;
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

    public PictureLightDto(@NotNull Integer id) {
        this.id = id;
    }

    public @NotNull Integer getId() {
        return id;
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