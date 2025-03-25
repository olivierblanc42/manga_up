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
    private Manga idMangas;

    @Column(name = "quantity")
    private Integer quantity;

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

    public Manga getIdMangas() {
        return idMangas;
    }

    public void setIdMangas(Manga idMangas) {
        this.idMangas = idMangas;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}