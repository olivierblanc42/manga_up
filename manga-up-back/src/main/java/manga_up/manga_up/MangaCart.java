package manga_up.manga_up;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "manga_cart", schema = "manga_up")
public class MangaCart {
    @EmbeddedId
    private MangaCartId id;

    @MapsId("idCart")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_cart", nullable = false)
    private Cart idCart;

    @MapsId("idMangas")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_mangas", nullable = false)
    private Manga idMangas;

    @Column(name = "quantit")
    private Integer quantit;

}