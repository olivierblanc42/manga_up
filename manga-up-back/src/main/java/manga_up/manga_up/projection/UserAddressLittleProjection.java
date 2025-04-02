package manga_up.manga_up.projection;

import java.time.LocalDateTime;

public interface UserAddressLittleProjection {
    String getLine1();
    String getLine2();
    String getLine3();
    String getCity();
    String getPostalCode();
}
