package manga_up.manga_up.projection.manga;


public interface MangaBaseProjection {
    Integer getId();
    String getTitle();
    Integer getPictureId();
    String getPictureUrl();
    String getAuthorFullName();
}
