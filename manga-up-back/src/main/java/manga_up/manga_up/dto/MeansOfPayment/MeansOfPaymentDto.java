package manga_up.manga_up.dto.MeansOfPayment;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.MeansOfPayment}
 */
public class MeansOfPaymentDto implements Serializable {
    private final Integer id;
    @Size(max = 50)
    private final String label;

    public MeansOfPaymentDto(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeansOfPaymentDto entity = (MeansOfPaymentDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.label, entity.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "label = " + label + ")";
    }
}