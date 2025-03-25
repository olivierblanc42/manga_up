package manga_up.manga_up.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mangas_authors", schema = "manga_up")
public class MangasAuthor {
    @EmbeddedId
    private MangasAuthorId id;

    @MapsId("idMangas")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_mangas", nullable = false)
    private Manga idMangas;

    @MapsId("idAuthors")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_authors", nullable = false)
    private Author idAuthors;

    public MangasAuthorId getId() {
        return id;
    }

    public void setId(MangasAuthorId id) {
        this.id = id;
    }

    public Manga getIdMangas() {
        return idMangas;
    }

    public void setIdMangas(Manga idMangas) {
        this.idMangas = idMangas;
    }

    public Author getIdAuthors() {
        return idAuthors;
    }

    public void setIdAuthors(Author idAuthors) {
        this.idAuthors = idAuthors;
    }

}