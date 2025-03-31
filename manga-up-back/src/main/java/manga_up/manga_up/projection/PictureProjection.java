package manga_up.manga_up.projection;

import java.util.Set;

public interface PictureProjection {
    Integer getId();
    String getUrl();
    MangaLittleProjection getManga();
}
