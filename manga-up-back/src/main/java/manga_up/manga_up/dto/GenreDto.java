package manga_up.manga_up.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Genre}
 */
public class GenreDto implements Serializable {
    private Integer id;
    @NotNull
    @Size(max = 50)
    private String label;
    private Instant createdAt;

    public GenreDto() {
    }

    public GenreDto(Integer id, String label, Instant createdAt) {
        this.id = id;
        this.label = label;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreDto entity = (GenreDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.label, entity.label) &&
                Objects.equals(this.createdAt, entity.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, createdAt);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "label = " + label + ", " +
                "createdAt = " + createdAt + ")";
    }
}