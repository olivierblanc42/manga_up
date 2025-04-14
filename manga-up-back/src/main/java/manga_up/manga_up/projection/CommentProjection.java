package manga_up.manga_up.projection;

public interface CommentProjection {
    Integer id();
    Integer rating();
    String comment();
    AppUserLittleProjection idUsers();
    MangaLittleProjection idMangas();

}
