package manga_up.manga_up.projection;

import java.time.LocalDateTime;
import java.util.Set;

public interface GenreProjection {
    Integer getId();
    String getLabel();
    String getUrl();
    LocalDateTime getCreatedAt();
    Set<MangaLittleProjection> getMangas();
}

