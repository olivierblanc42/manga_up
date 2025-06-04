package manga_up.manga_up.dto.genre;


import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.projection.genre.GenreProjection;

import org.springframework.data.domain.Page;

public class GenreWithMangasResponse {
    private GenreProjection genre;
    private Page<MangaDtoRandom> mangas;

    public GenreWithMangasResponse(GenreProjection genre, Page<MangaDtoRandom> mangas) {
        this.genre = genre;
        this.mangas = mangas;
    }

    public GenreProjection getGenre() {
        return genre;
    }

    public void setGenre(GenreProjection genre) {
        this.genre = genre;
    }

    public Page<MangaDtoRandom> getMangas() {
        return mangas;
    }

    public void setMangas(Page<MangaDtoRandom> mangas) {
        this.mangas = mangas;
    }
}
