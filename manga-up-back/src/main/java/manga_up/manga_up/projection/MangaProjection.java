package manga_up.manga_up.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public interface MangaProjection {
    Integer getId();
    String getTitle();
    String getSubtitle();
    LocalDateTime getReleaseDate();
    BigDecimal getPrice();
    BigDecimal getPriceHt();
    Boolean getInStock();
    Boolean getActive();
    CategoryLittleProjection getIdCategories();
    Set<GenreLittleProjection> getGenres();
    Set<AuthorLittleProjection> getAuthors();
}
