package manga_up.manga_up.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link manga_up.manga_up.model.UserAddress}
 */
@Component
public class UserAddressDto implements Serializable {
    @NotNull
    @Size(max = 50)
    private String line1;
    @Size(max = 50)
    private String line2;
    @Size(max = 50)
    private String line3;
    @NotNull
    @Size(max = 100)
    private String city;
    private Instant createdAt;
    @NotNull
    @Size(max = 5)
    private String postalCode;

    public UserAddressDto() {
    }

    public UserAddressDto( String line1, String line2, String line3, String city, Instant createdAt, String postalCode) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.city = city;
        this.createdAt = createdAt;
        this.postalCode = postalCode;
    }


    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAddressDto entity = (UserAddressDto) o;
        return
                Objects.equals(this.line1, entity.line1) &&
                Objects.equals(this.line2, entity.line2) &&
                Objects.equals(this.line3, entity.line3) &&
                Objects.equals(this.city, entity.city) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.postalCode, entity.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line1, line2, line3, city, createdAt, postalCode);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "line1 = " + line1 + ", " +
                "line2 = " + line2 + ", " +
                "line3 = " + line3 + ", " +
                "city = " + city + ", " +
                "createdAt = " + createdAt + ", " +
                "postalCode = " + postalCode + ")";
    }
}