package manga_up.manga_up.projection.genderUser;

import java.util.Set;

import manga_up.manga_up.projection.appUser.AppUserLittleProjection;

public interface GenderUserProjection {
      Integer getId();
      String getLabel();
      Set<AppUserLittleProjection> getAppUsers();
}
