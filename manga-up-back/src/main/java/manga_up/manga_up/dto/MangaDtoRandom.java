package manga_up.manga_up.dto;



import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link manga_up.manga_up.model.Manga}
 */
public class MangaDtoRandom implements Serializable {
    private Integer Id_mangas;
    private String title;
    private Set<AuthorDtoRandom> authors;
    private PictureDtoRandom picture;


    public MangaDtoRandom(Integer id_mangas, String title, Set<AuthorDtoRandom> authors, PictureDtoRandom picture) {
        Id_mangas = id_mangas;
        this.title = title;
        this.authors = authors;
        this.picture = picture;

    }

    public Set<AuthorDtoRandom> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDtoRandom> authors) {
        this.authors = authors;
    }

    public PictureDtoRandom getPicture() {
        return picture;
    }

    public void setPictures(PictureDtoRandom picture) {
        this.picture = picture;
    }

    public Integer getId_mangas() {
        return Id_mangas;
    }

    public void setId_mangas(Integer id_mangas) {
        Id_mangas = id_mangas;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MangaDtoRandom that = (MangaDtoRandom) o;
        return Objects.equals(Id_mangas, that.Id_mangas) && Objects.equals(title, that.title) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id_mangas, title);
    }


    @Override
    public String toString() {
        return "MangaDtoRandom{" +
                "Id_mangas=" + Id_mangas +
                ", title='" + title ;
    }
}