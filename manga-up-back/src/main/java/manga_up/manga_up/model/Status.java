package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "status", schema = "manga_up")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_status", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "label", length = 50)
    private String label;

}