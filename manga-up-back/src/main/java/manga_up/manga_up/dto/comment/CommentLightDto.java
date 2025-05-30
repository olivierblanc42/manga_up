package manga_up.manga_up.dto.comment;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Comment}
 */
public class CommentLightDto implements Serializable {
    private final Integer rating;
    private final String comment;

    public CommentLightDto(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLightDto entity = (CommentLightDto) o;
        return Objects.equals(this.rating, entity.rating) &&
                Objects.equals(this.comment, entity.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, comment);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "rating = " + rating + ", " +
                "comment = " + comment + ")";
    }
}