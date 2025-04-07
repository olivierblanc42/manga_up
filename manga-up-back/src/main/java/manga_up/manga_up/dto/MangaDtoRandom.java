package manga_up.manga_up.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link manga_up.manga_up.model.Manga}
 */
public class MangaDtoRandom implements Serializable {
    private Integer Id_mangas;
    private String title;
    private Integer Id_authors;
    private String lastname;
    private String firstname;
    private Integer Id_picture;
    private String url;

    public MangaDtoRandom(Integer id_mangas, String title, Integer id_authors, String firstname, String lastname, Integer id_picture, String url) {
        Id_mangas = id_mangas;
        this.title = title;
        Id_authors = id_authors;
        this.firstname = firstname;
        this.lastname = lastname;
        Id_picture = id_picture;
        this.url = url;
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

    public Integer getId_authors() {
        return Id_authors;
    }

    public void setId_authors(Integer id_authors) {
        Id_authors = id_authors;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getId_picture() {
        return Id_picture;
    }

    public void setId_picture(Integer id_picture) {
        Id_picture = id_picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MangaDtoRandom that = (MangaDtoRandom) o;
        return Objects.equals(Id_mangas, that.Id_mangas) && Objects.equals(title, that.title) && Objects.equals(Id_authors, that.Id_authors) && Objects.equals(lastname, that.lastname) && Objects.equals(firstname, that.firstname) && Objects.equals(Id_picture, that.Id_picture) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id_mangas, title, Id_authors, lastname, firstname, Id_picture, url);
    }


    @Override
    public String toString() {
        return "MangaDtoRandom{" +
                "Id_mangas=" + Id_mangas +
                ", title='" + title + '\'' +
                ", Id_authors='" + Id_authors + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", Id_picture='" + Id_picture + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}