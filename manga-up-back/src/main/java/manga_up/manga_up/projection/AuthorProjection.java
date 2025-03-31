package manga_up.manga_up.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface AuthorProjection {
    Integer getId();
    String getFirstname();
    String getLastname();
    String getDescription();
    LocalDate getCreatedAt();
    Set<MangaLittleProjection> getMangas();
}
