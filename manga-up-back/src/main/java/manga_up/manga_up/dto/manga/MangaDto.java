package manga_up.manga_up.dto.manga;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.dto.picture.PictureLightDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link manga_up.manga_up.model.Manga}
 */
public class MangaDto implements Serializable {
    @NotNull
    @Size(max = 255)
    private final String title;
    @Size(max = 255)
    private final String subtitle;
    private final Instant releaseDate;
    @Size(max = 1000)
    private final String summary;
    private final BigDecimal priceHt;
    private final BigDecimal price;
    private final Boolean inStock;
    private final Boolean active;
    @NotNull
    private final CategoryLittleDto idCategories;
    private final Set<Integer> genres;
    private final Set<Integer> authors;
    @NotNull(message = "A manga must have at least one image.")
    @Size(min = 1, message = "A manga must contain at least one image.")
    private final Set<PictureLightDto> pictures;
    private final Set<UserFavoriteDto> usersFavorites;

    public MangaDto(String title, String subtitle, Instant releaseDate, String summary, BigDecimal priceHt,BigDecimal price, Boolean inStock, Boolean active, CategoryLittleDto idCategories, Set<Integer> genres, Set<Integer> authors, Set<PictureLightDto> pictures, Set<UserFavoriteDto> usersFavorites) {
        this.title = title;
        this.subtitle = subtitle;
        this.releaseDate = releaseDate;
        this.summary = summary;
        this.priceHt = priceHt;
        this.price = price;
        this.inStock = inStock;
        this.active = active;
        this.idCategories = idCategories;
        this.genres = genres;
        this.authors = authors;
        this.pictures = pictures;
        this.usersFavorites = usersFavorites;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public String getSummary() {
        return summary;
    }

    public BigDecimal getPriceHt() {
        return priceHt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public Boolean getActive() {
        return active;
    }

    public CategoryLittleDto getIdCategories() {
        return idCategories;
    }

    public Set<Integer> getGenres() {
        return genres;
    }

    public Set<Integer> getAuthors() {
        return authors;
    }

    public Set<PictureLightDto> getPictures() {
        return pictures;
    }

    public Set<UserFavoriteDto> getUsersFavorites() {
        return usersFavorites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MangaDto entity = (MangaDto) o;
        return Objects.equals(this.title, entity.title) &&
                Objects.equals(this.subtitle, entity.subtitle) &&
                Objects.equals(this.releaseDate, entity.releaseDate) &&
                Objects.equals(this.summary, entity.summary) &&
                Objects.equals(this.priceHt, entity.priceHt) &&
                Objects.equals(this.inStock, entity.inStock) &&
                Objects.equals(this.active, entity.active) &&
                Objects.equals(this.idCategories, entity.idCategories) &&
                Objects.equals(this.genres, entity.genres) &&
                Objects.equals(this.authors, entity.authors) &&
                Objects.equals(this.pictures, entity.pictures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subtitle, releaseDate, summary, priceHt, inStock, active, idCategories, genres, authors, pictures);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "title = " + title + ", " +
                "subtitle = " + subtitle + ", " +
                "releaseDate = " + releaseDate + ", " +
                "summary = " + summary + ", " +
                "priceHt = " + priceHt + ", " +
                "inStock = " + inStock + ", " +
                "active = " + active + ", " +
                "idCategories = " + idCategories + ", " +
                "genres = " + genres + ", " +
                "authors = " + authors + ", " +
                "pictures = " + pictures + ")";
    }
}