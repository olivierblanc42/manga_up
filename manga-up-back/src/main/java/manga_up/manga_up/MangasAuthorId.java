package manga_up.manga_up;

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
public class MangasAuthorId implements Serializable {
    private static final long serialVersionUID = -5903983671267241951L;
    @NotNull
    @Column(name = "Id_mangas", nullable = false)
    private Integer idMangas;

    @NotNull
    @Column(name = "Id_authors", nullable = false)
    private Integer idAuthors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MangasAuthorId entity = (MangasAuthorId) o;
        return Objects.equals(this.idMangas, entity.idMangas) &&
                Objects.equals(this.idAuthors, entity.idAuthors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMangas, idAuthors);
    }

}