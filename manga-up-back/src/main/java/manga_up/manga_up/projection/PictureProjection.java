package manga_up.manga_up.projection;


public interface PictureProjection {
    Integer getId();
    String getUrl();
    Boolean getIsMain();
    MangaLittleProjection getIdMangas();
}
