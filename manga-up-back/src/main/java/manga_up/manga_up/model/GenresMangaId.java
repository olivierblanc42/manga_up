package manga_up.manga_up.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class GenresMangaId implements Serializable {
    private static final long serialVersionUID = 2381404664164705861L;
    @NotNull
    @Column(name = "Id_mangas", nullable = false)
    private Integer idMangas;

    @NotNull
    @Column(name = "Id_gender_mangas", nullable = false)
    private Integer idGenderMangas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GenresMangaId entity = (GenresMangaId) o;
        return Objects.equals(this.idMangas, entity.idMangas) &&
                Objects.equals(this.idGenderMangas, entity.idGenderMangas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMangas, idGenderMangas);
    }

}