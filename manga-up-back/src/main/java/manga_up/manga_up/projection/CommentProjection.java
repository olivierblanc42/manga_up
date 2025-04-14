package manga_up.manga_up.projection;

public interface CommentProjection {
    Integer getId();
    Integer getRating();
    String getComment();
    AppUserLittleProjection getIdUsers();
    MangaLittleProjection getIdMangas();

}
