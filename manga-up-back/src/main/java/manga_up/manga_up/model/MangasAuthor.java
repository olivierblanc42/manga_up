package manga_up.manga_up.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}