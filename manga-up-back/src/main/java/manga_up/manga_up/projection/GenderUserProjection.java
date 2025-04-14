package manga_up.manga_up.projection;

import java.util.Set;

public interface GenderUserProjection {
      Integer getId();
      String getLabel();
      Set<AppUserLittleProjection> getAppUsers();
}
