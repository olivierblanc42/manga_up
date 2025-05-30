package manga_up.manga_up.projection.author;

import java.time.LocalDate;


public interface AuthorProjection {
    Integer getId();
    String getFirstname();
    String getLastname();
    String getDescription();
    LocalDate getCreatedAt();
    LocalDate getBirthdate();
    String getUrl();
    String getGenre();    
}
