package manga_up.manga_up.projection.genre;

import java.time.LocalDateTime;

public interface GenreLittleProjection {
    Integer getId();
    String getLabel();
    LocalDateTime getCreatedAt();
}
