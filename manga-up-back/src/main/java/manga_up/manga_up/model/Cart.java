package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "cart", schema = "manga_up")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_cart", nullable = false)
    private Integer id;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "validation_date")
    private Instant validationDate;

    @Column(name = "invoice_date")
    private Instant invoiceDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_status", nullable = false)
    private Status idStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_means_of_payment", nullable = false)
    private MeansOfPayment idMeansOfPayment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_adress", nullable = false)
    private Address idAdress;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_users", nullable = false)
    private AppUser idUsers;

}