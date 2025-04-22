package manga_up.manga_up.dto;


import manga_up.manga_up.projection.GenreProjection;
import manga_up.manga_up.projection.MangaBaseProjection;
import org.springframework.data.domain.Page;

public class GenreWithMangasResponse {
    private GenreProjection genre;
    private Page<MangaBaseProjection> mangas;

    public GenreWithMangasResponse(GenreProjection genre, Page<MangaBaseProjection> mangas) {
        this.genre = genre;
        this.mangas = mangas;
    }

    public GenreProjection getGenre() {
        return genre;
    }

    public void setGenre(GenreProjection genre) {
        this.genre = genre;
    }

    public Page<MangaBaseProjection> getMangas() {
        return mangas;
    }

    public void setMangas(Page<MangaBaseProjection> mangas) {
        this.mangas = mangas;
    }
}
