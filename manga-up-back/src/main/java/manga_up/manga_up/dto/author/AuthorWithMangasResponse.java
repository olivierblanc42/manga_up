package manga_up.manga_up.dto.author;

import org.springframework.data.domain.Page;

import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.projection.author.AuthorProjection;


public class AuthorWithMangasResponse {
    private AuthorProjection author; 
    private Page<MangaDtoRandom> mangas; 

    public AuthorWithMangasResponse(AuthorProjection author, Page<MangaDtoRandom> mangas) {
        this.author = author;
        this.mangas = mangas;
    }

    public AuthorProjection getAuthor() {
        return author;
    }

    public void setAuthor(AuthorProjection author) {
        this.author = author;
    }

    public Page<MangaDtoRandom> getMangas() {
        return mangas;
    }

    public void setMangas(Page<MangaDtoRandom> mangas) {
        this.mangas = mangas;
    }
}
