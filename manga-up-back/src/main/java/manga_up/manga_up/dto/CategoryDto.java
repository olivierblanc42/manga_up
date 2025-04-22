package manga_up.manga_up.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Category}
 */
public class CategoryDto implements Serializable {
    @NotNull
    @Size(max = 50)
    private final String label;
    @NotNull
    private final String description;



    public CategoryDto(String label, String description) {
        this.label = label;
        this.description = description;


    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDto entity = (CategoryDto) o;
        return Objects.equals(this.label, entity.label) &&
                Objects.equals(this.description, entity.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, description);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "label = " + label + ", " +
                "description = " + description ;
    }
}