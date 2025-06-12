package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * Represents a shopping cart entity in the manga application.
 * Stores information about the creation, validation, and invoicing of the cart,
 * as well as relationships with status, payment method, user address, and user.
 */
@Entity
@Table(name = "cart", schema = "manga_up")
public class Cart {

    /** Unique identifier of the cart. */
    @Id
    @Column(name = "Id_cart", nullable = false)
    private Integer id;

    /** Timestamp when the cart was created. */
    @Column(name = "date_created")
    private Instant dateCreated;

    /** Timestamp when the cart was validated. */
    @Column(name = "validation_date")
    private Instant validationDate;

    /** Timestamp when the invoice was generated. */
    @Column(name = "invoice_date")
    private Instant invoiceDate;

    /** Status of the cart (e.g., pending, completed, cancelled). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_status", nullable = false)
    private Status idStatus;

    /** Payment method associated with the cart. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_means_of_payment", nullable = false)
    private MeansOfPayment idMeansOfPayment;

    /** Shipping or billing address for the cart. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_user_address", nullable = false)
    private UserAddress idUserAddress;

    /** User who owns the cart. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_users", nullable = false)
    private AppUser idUsers;

    // --- Getters and setters ---

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
