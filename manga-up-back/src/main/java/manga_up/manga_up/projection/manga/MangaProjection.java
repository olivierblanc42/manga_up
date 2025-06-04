package manga_up.manga_up.projection.manga;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import manga_up.manga_up.projection.author.AuthorLittleProjection;
import manga_up.manga_up.projection.category.CategoryLittleProjection;
import manga_up.manga_up.projection.genre.GenreLittleProjection;
import manga_up.manga_up.projection.pictureProjection.PictureLittleProjection;

public interface MangaProjection {
    Integer getId();
    String getTitle();
    String getSubtitle();
    String getSummary();
    LocalDateTime getReleaseDate();
    BigDecimal getPrice();
    BigDecimal getPriceHt();
    Boolean getInStock();
    Boolean getActive();
    CategoryLittleProjection getIdCategories();
    Set<PictureLittleProjection> getPictures();
    Set<GenreLittleProjection> getGenres();
    Set<AuthorLittleProjection> getAuthors();

}
