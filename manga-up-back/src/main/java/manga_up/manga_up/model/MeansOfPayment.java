package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "means_of_payment", schema = "manga_up")
public class MeansOfPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_means_of_payment", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "label", length = 50)
    private String label;

}