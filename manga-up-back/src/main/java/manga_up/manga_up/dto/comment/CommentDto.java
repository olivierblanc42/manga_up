package manga_up.manga_up.dto.comment;

import jakarta.validation.constraints.NotNull;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Manga;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Comment}
 */
public class CommentDto implements Serializable {
    private final Integer id;
    private final Integer rating;
    private final String comment;
    private final Instant createdAt;
    @NotNull
    private final Manga idMangas;
    @NotNull
    private final AppUser idUsers;

    public CommentDto(Integer id, Integer rating, String comment, Instant createdAt, Manga idMangas, AppUser idUsers) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.idMangas = idMangas;
        this.idUsers = idUsers;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Manga getIdMangas() {
        return idMangas;
    }

    public AppUser getIdUsers() {
        return idUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDto entity = (CommentDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.rating, entity.rating) &&
                Objects.equals(this.comment, entity.comment) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.idMangas, entity.idMangas) &&
                Objects.equals(this.idUsers, entity.idUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, comment, createdAt, idMangas, idUsers);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "rating = " + rating + ", " +
                "comment = " + comment + ", " +
                "createdAt = " + createdAt + ", " +
                "idMangas = " + idMangas + ", " +
                "idUsers = " + idUsers + ")";
    }
}