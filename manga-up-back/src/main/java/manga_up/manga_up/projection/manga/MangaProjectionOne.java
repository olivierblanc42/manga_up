package manga_up.manga_up.projection.manga;

import java.math.BigDecimal;

public interface MangaProjectionOne {
    Integer getId_mangas();
    String getTitle();
    String getSubtitle();
    String getSummary();
    BigDecimal getPrice();
    String getCategory(); 
    String getGenres(); 
    String getAuthors(); 
    String getPicture();
}
