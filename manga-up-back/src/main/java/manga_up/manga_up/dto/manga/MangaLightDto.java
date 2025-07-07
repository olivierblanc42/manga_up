package manga_up.manga_up.dto.manga;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import manga_up.manga_up.dto.picture.PictureLightDto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link manga_up.manga_up.model.Manga}
 */
public class MangaLightDto implements Serializable {
    private final Integer id;
    @NotNull
    @Size(max = 255)
    private final String title;
    private final PictureLightDto pictures;

    public MangaLightDto(Integer id, String title, PictureLightDto pictures) {
        this.id = id;
        this.title = title;
        this.pictures = pictures;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public PictureLightDto getPicture(){
        return pictures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MangaLightDto entity = (MangaLightDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.title, entity.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ")";
    }
}