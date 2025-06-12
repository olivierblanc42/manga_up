package manga_up.manga_up.dto.genderUser;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.GenderUser}
 */
public class GenderUserDto implements Serializable {
    @NotNull
    private  Integer id;

    @NotNull
    @Size(max = 50)
    private String label;

 

    public GenderUserDto(Integer id, String label) {

        this.id = id;
        this.label = label;
    }



    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenderUserDto entity = (GenderUserDto) o;
        return
                Objects.equals(this.label, entity.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash( label);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "label = " + label + ")";
    }
}