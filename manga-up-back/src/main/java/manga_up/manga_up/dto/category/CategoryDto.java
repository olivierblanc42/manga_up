package manga_up.manga_up.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.Category}
 */
public class CategoryDto implements Serializable {
    private final Integer id;
    @NotNull
    @Size(max = 50)
    private final String label;
    @NotNull
    private final String description;
    private final String url;


    public CategoryDto( Integer id,String label, String description,String url) {
        this.id= id;
        this.label = label;
        this.description = description;
        this.url =url;

    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl(){
        return url;
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