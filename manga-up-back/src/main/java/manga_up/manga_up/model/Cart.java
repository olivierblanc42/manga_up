package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "cart", schema = "manga_up")
public class Cart {
    @Id
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
    @JoinColumn(name = "Id_user_address", nullable = false)
    private UserAddress idUserAddress;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_users", nullable = false)
    private AppUser idUsers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Instant validationDate) {
        this.validationDate = validationDate;
    }

    public Instant getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Instant invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Status getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Status idStatus) {
        this.idStatus = idStatus;
    }

    public MeansOfPayment getIdMeansOfPayment() {
        return idMeansOfPayment;
    }

    public void setIdMeansOfPayment(MeansOfPayment idMeansOfPayment) {
        this.idMeansOfPayment = idMeansOfPayment;
    }

    public UserAddress getIdUserAddress() {
        return idUserAddress;
    }

    public void setIdUserAddress(UserAddress idUserAddress) {
        this.idUserAddress = idUserAddress;
    }

    public AppUser getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(AppUser idUsers) {
        this.idUsers = idUsers;
    }

}