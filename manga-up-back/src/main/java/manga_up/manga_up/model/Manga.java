package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "manga", schema = "manga_up")
public class Manga {
    @Id
    @Column(name = "Id_mangas", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "release_date")
    private Instant releaseDate;

    @Size(max = 50)
    @Column(name = "summary", length = 50)
    private String summary;

    @Size(max = 50)
    @Column(name = "price", length = 50)
    private String price;

    @Column(name = "price_ht", precision = 5, scale = 2)
    private BigDecimal priceHt;

    @Column(name = "in_stock")
    private Boolean inStock;

    @Column(name = "active")
    private Boolean active;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_categories", nullable = false)
    private Category idCategories;

}