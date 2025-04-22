package manga_up.manga_up.dto;

import org.springframework.data.domain.Page;

import manga_up.manga_up.projection.AuthorProjection;
import manga_up.manga_up.projection.MangaBaseProjection;


public class AuthorWithMangasResponse {
    private AuthorProjection author; 
    private Page<MangaBaseProjection> mangas; 

    public AuthorWithMangasResponse(AuthorProjection author, Page<MangaBaseProjection> mangas) {
        this.author = author;
        this.mangas = mangas;
    }

    public AuthorProjection getAuthor() {
        return author;
    }

    public void setAuthor(AuthorProjection author) {
        this.author = author;
    }

    public Page<MangaBaseProjection> getMangas() {
        return mangas;
    }

    public void setMangas(Page<MangaBaseProjection> mangas) {
        this.mangas = mangas;
    }
}
