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
    private String title;
    private Integer id;
    private String lastname;
    private String firstname;
    private String url;

    public MangaDtoRandom(String title, Integer id, String lastname, String firstname, String url) {
        this.title = title;
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return Objects.equals(title, that.title) && Objects.equals(id, that.id) && Objects.equals(lastname, that.lastname) && Objects.equals(firstname, that.firstname) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id, lastname, firstname, url);
    }

    @Override
    public String toString() {
        return "MangaDtoRandom{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}