package manga_up.manga_up.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "picture", schema = "manga_up")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_picture", nullable = false)
    private Integer id;

    @Column(name = "is_main")
    private Boolean isMain;

    @Size(max = 255)
    @Column(name = "url")
    private String url;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_mangas", nullable = false)
    private Manga idMangas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Manga getIdMangas() {
        return idMangas;
    }

    public void setIdMangas(Manga idMangas) {
        this.idMangas = idMangas;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Picture))
            return false;
        Picture other = (Picture) o;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        // Si id null, on compare sur url et isMain
        return (url != null ? url.equals(other.url) : other.url == null)
                && (isMain != null ? isMain.equals(other.isMain) : other.isMain == null);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        int result = (url != null) ? url.hashCode() : 0;
        result = 31 * result + ((isMain != null) ? isMain.hashCode() : 0);
        return result;
    }
    
}