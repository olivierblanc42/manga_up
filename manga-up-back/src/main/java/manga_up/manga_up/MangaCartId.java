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
public class MangaCartId implements Serializable {
    private static final long serialVersionUID = 4312962114909606649L;
    @NotNull
    @Column(name = "Id_cart", nullable = false)
    private Integer idCart;

    @NotNull
    @Column(name = "Id_mangas", nullable = false)
    private Integer idMangas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MangaCartId entity = (MangaCartId) o;
        return Objects.equals(this.idMangas, entity.idMangas) &&
                Objects.equals(this.idCart, entity.idCart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMangas, idCart);
    }

}