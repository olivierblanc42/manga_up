package manga_up.manga_up.projection.genre;

import java.time.LocalDateTime;

public interface GenreProjection {
    Integer getId();
    String getLabel();
    String getUrl();
    LocalDateTime getCreatedAt();
}

