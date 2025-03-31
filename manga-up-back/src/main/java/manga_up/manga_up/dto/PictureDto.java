package manga_up.manga_up.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import manga_up.manga_up.model.Manga;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Picture}
 */
public class PictureDto implements Serializable {
    private final Integer id;
    @Size(max = 50)
    private final String name;
    @Size(max = 255)
    private final String url;
    @NotNull
    private final Manga idMangas;

    public PictureDto(Integer id, String name, String url, Manga idMangas) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.idMangas = idMangas;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Manga getIdMangas() {
        return idMangas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureDto entity = (PictureDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.url, entity.url) &&
                Objects.equals(this.idMangas, entity.idMangas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, idMangas);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "url = " + url + ", " +
                "idMangas = " + idMangas + ")";
    }
}