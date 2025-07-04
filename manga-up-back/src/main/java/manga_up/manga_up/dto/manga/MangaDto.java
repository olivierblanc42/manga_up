package manga_up.manga_up.dto.manga;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.dto.appUser.UserFavoriteDto;

/**
 * DTO for {@link manga_up.manga_up.model.Manga}
 */
public class MangaDto implements Serializable {

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String subtitle;

    private Instant releaseDate;

    @Size(max = 1000)
    private String summary;

    private BigDecimal priceHt;
    private BigDecimal price;

    private Boolean inStock;
    private Boolean active;

    @NotNull
    private CategoryLittleDto idCategories;

    private Set<Integer> genres;
    private Set<Integer> authors;

    @NotNull(message = "A manga must have at least one image.")
    @Size(min = 1, message = "A manga must contain at least one image.")
    private Set<PictureLightDto> pictures;

    private Set<UserFavoriteDto> usersFavorites;

    // No-arg constructor for Jackson
    public MangaDto() {
    }

    // All-args constructor
    public MangaDto(String title,
            String subtitle,
            Instant releaseDate,
            String summary,
            BigDecimal priceHt,
            BigDecimal price,
            Boolean inStock,
            Boolean active,
            CategoryLittleDto idCategories,
            Set<Integer> genres,
            Set<Integer> authors,
            Set<PictureLightDto> pictures,
            Set<UserFavoriteDto> usersFavorites) {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public BigDecimal getPriceHt() {
        return priceHt;
    }

    public void setPriceHt(BigDecimal priceHt) {
        this.priceHt = priceHt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CategoryLittleDto getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(CategoryLittleDto idCategories) {
        this.idCategories = idCategories;
    }

    public Set<Integer> getGenres() {
        return genres;
    }

    public void setGenres(Set<Integer> genres) {
        this.genres = genres;
    }

    public Set<Integer> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Integer> authors) {
        this.authors = authors;
    }

    public Set<PictureLightDto> getPictures() {
        return pictures;
    }

    public void setPictures(Set<PictureLightDto> pictures) {
        this.pictures = pictures;
    }

    public Set<UserFavoriteDto> getUsersFavorites() {
        return usersFavorites;
    }

    public void setUsersFavorites(Set<UserFavoriteDto> usersFavorites) {
        this.usersFavorites = usersFavorites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MangaDto))
            return false;
        MangaDto that = (MangaDto) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(subtitle, that.subtitle) &&
                Objects.equals(releaseDate, that.releaseDate) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(priceHt, that.priceHt) &&
                Objects.equals(price, that.price) &&
                Objects.equals(inStock, that.inStock) &&
                Objects.equals(active, that.active) &&
                Objects.equals(idCategories, that.idCategories) &&
                Objects.equals(genres, that.genres) &&
                Objects.equals(authors, that.authors) &&
                Objects.equals(pictures, that.pictures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subtitle, releaseDate, summary, priceHt, price, inStock, active, idCategories,
                genres, authors, pictures);
    }

    @Override
    public String toString() {
        return "MangaDto(" +
                "title=" + title +
                ", subtitle=" + subtitle +
                ", releaseDate=" + releaseDate +
                ", summary=" + summary +
                ", priceHt=" + priceHt +
                ", price=" + price +
                ", inStock=" + inStock +
                ", active=" + active +
                ", idCategories=" + idCategories +
                ", genres=" + genres +
                ", authors=" + authors +
                ", pictures=" + pictures +
                ")";
    }
}
