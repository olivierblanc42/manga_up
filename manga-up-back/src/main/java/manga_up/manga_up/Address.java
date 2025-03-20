package manga_up.manga_up;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "manga_up")
public class Address {
    @Id
    @Column(name = "Id_adress", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "line1", nullable = false, length = 50)
    private String line1;

    @Size(max = 50)
    @Column(name = "line2", length = 50)
    private String line2;

    @Size(max = 50)
    @Column(name = "line3", length = 50)
    private String line3;

    @Size(max = 100)
    @NotNull
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 5)
    @NotNull
    @Column(name = "postal_code", nullable = false, length = 5)
    private String postalCode;

}