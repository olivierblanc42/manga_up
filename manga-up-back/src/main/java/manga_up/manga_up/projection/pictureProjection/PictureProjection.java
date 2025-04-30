package manga_up.manga_up.projection.pictureProjection;

import manga_up.manga_up.projection.manga.MangaLittleProjection;

public interface PictureProjection {
    Integer getId();
    String getUrl();
    Boolean getIsMain();
    MangaLittleProjection getIdMangas();
}
