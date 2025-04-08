package manga_up.manga_up.projection;

import java.time.LocalDateTime;
import java.util.Set;

public interface UserAddressProjection {
     Integer getId();
     String getLine1();
     String getLine2();
     String getLine3();
     String getCity();
     LocalDateTime getCreatedAt();
     String getPostalCode();
     Set<AppUserLittleProjection> getAppUsers();

}
