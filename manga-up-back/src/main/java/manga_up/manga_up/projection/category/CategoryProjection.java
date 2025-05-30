package manga_up.manga_up.projection.category;


import java.time.LocalDateTime;

public interface CategoryProjection {
    Integer getId();
    String getLabel();
    LocalDateTime getCreatedAt();
    String getDescription();
    String getUrl();
}
