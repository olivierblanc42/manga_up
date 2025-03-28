package manga_up.manga_up.projection;

import java.time.LocalDateTime;
import java.util.Set;

public interface GenreProjection {
    Integer getId();
    String getLabel();
    LocalDateTime getCreatedAt();
    Set<MangaProjection> getMangas();
}

