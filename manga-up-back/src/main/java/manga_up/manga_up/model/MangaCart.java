package manga_up.manga_up.model;

import jakarta.persistence.*;

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
    private Mangas idMangas;

    @Column(name = "quantit")
    private Integer quantit;

    public MangaCartId getId() {
        return id;
    }

    public void setId(MangaCartId id) {
        this.id = id;
    }

    public Cart getIdCart() {
        return idCart;
    }

    public void setIdCart(Cart idCart) {
        this.idCart = idCart;
    }

    public Mangas getIdMangas() {
        return idMangas;
    }

    public void setIdMangas(Mangas idMangas) {
        this.idMangas = idMangas;
    }

    public Integer getQuantit() {
        return quantit;
    }

    public void setQuantit(Integer quantit) {
        this.quantit = quantit;
    }

}