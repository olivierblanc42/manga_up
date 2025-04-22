package manga_up.manga_up.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link manga_up.manga_up.model.Manga}
 */
public class MangaDtoOne implements Serializable {
    private final Integer id;
    @NotNull
    @Size(max = 255)
    private final String title;
    @Size(max = 255)
    private final String subtitle;
    private final String summary;
    private final BigDecimal price;
    @NotNull
    private final CategoryDto idCategories;
    private final Set<GenreDto> genres;
    private final Set<AuthorDtoRandom> authors;
    private final PictureDtoRandom picture;

    public MangaDtoOne(Integer id, String title, String subtitle,  String summary, BigDecimal price, CategoryDto idCategories, Set<GenreDto> genres, Set<AuthorDtoRandom> authors, PictureDtoRandom picture) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.summary = summary;
        this.price = price;
        this.idCategories = idCategories;
        this.genres = genres;
        this.authors = authors;
        this.picture = picture;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }



    public String getSummary() {
        return summary;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CategoryDto getIdCategories() {
        return idCategories;
    }

    public Set<GenreDto> getGenres() {
        return genres;
    }

    public Set<AuthorDtoRandom> getAuthors() {
        return authors;
    }

    public PictureDtoRandom getPicture() {
        return picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MangaDtoOne entity = (MangaDtoOne) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.subtitle, entity.subtitle) &&
                Objects.equals(this.summary, entity.summary) &&
                Objects.equals(this.price, entity.price) &&
                Objects.equals(this.idCategories, entity.idCategories) &&
                Objects.equals(this.genres, entity.genres) &&
                Objects.equals(this.authors, entity.authors) &&
                Objects.equals(this.picture, entity.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, subtitle, summary, price, idCategories, genres, authors, picture);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ", " +
                "subtitle = " + subtitle + ", " +
                "summary = " + summary + ", " +
                "price = " + price + ", " +
                "idCategories = " + idCategories + ", " +
                "genres = " + genres + ", " +
                "authors = " + authors + ", " +
                "pictures = " + picture + ")";
    }
}