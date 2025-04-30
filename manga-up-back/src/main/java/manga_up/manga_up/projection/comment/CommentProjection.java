package manga_up.manga_up.projection.comment;

import manga_up.manga_up.projection.appUser.AppUserLittleProjection;
import manga_up.manga_up.projection.manga.MangaLittleProjection;

public interface CommentProjection {
    Integer getId();
    Integer getRating();
    String getComment();
    AppUserLittleProjection getIdUsers();
    MangaLittleProjection getIdMangas();

}
