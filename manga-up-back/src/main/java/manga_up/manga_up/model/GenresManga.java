package manga_up.manga_up.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "genres_manga", schema = "manga_up")
public class GenresManga {
    @EmbeddedId
    private GenresMangaId id;

    @MapsId("idMangas")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_mangas", nullable = false)
    private Manga idMangas;

    @MapsId("idGenderMangas")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Id_gender_mangas", nullable = false)
    private Genre idGenderMangas;

    public GenresMangaId getId() {
        return id;
    }

    public void setId(GenresMangaId id) {
        this.id = id;
    }

    public Manga getIdMangas() {
        return idMangas;
    }

    public void setIdMangas(Manga idMangas) {
        this.idMangas = idMangas;
    }

    public Genre getIdGenderMangas() {
        return idGenderMangas;
    }

    public void setIdGenderMangas(Genre idGenderMangas) {
        this.idGenderMangas = idGenderMangas;
    }

}