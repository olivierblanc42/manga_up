package manga_up.manga_up.projection;

import java.time.LocalDateTime;

public interface GenreLittleProjection {
    Integer getId();
    String getLabel();
    LocalDateTime getCreatedAt();
}
