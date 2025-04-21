package manga_up.manga_up.projection;

import java.util.Set;

public interface MangaLittleProjection {
    Integer getId();
    String getTitle();
    Set<PictureLittleProjection> getPictures();
    
}
