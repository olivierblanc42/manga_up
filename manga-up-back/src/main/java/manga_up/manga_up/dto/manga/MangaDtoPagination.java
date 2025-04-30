package manga_up.manga_up.dto.manga;

import java.io.Serializable;
import java.util.Set;

import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.manga.MangaBaseProjection;

public class MangaDtoPagination implements Serializable {

    private MangaBaseProjection mangas;
    private Set<AuthorProjection> authors;

    public MangaDtoPagination() {
    }

    public MangaDtoPagination(MangaBaseProjection mangas, Set<AuthorProjection> authors) {
        this.mangas = mangas;
        this.authors = authors;
    }

    public MangaBaseProjection getMangas() {
        return mangas;
    }

    public void setMangas(MangaBaseProjection mangas) {
        this.mangas = mangas;
    }

    public Set<AuthorProjection> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorProjection> authors) {
        this.authors = authors;
    }
}
