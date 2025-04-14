package manga_up.manga_up.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface AuthorProjection {
    Integer id();
    String firstname();
    String lastname();
    String description();
    LocalDate createdAt();
    Set<MangaLittleProjection> mangas();
}
