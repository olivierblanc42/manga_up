package manga_up.manga_up.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Size(max = 3000, message = "Description must be at most 3000 characters.")
    private final String description;
    @Size(max = 255, message = "URL must be at most 255 characters.")
    @Pattern(regexp = "^(https?://)?([\\w.-]+)+(:\\d+)?(/\\S*)?$", message = "URL is not valid.")
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CategoryDto entity = (CategoryDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.label, entity.label) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.url, entity.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, description, url);
    }    

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "label = " + label + ", " +
                "description = " + description + ", " +
                "url = " + url + ")";
    }
    
}