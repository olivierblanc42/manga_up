package manga_up.manga_up.dto.category;

import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.projection.manga.MangaProjectionWithAuthor;

import org.springframework.data.domain.Page;

public class CategoryWithMangaResponse {

private CategoryProjection category;
private Page<MangaDtoRandom> mangas;

public CategoryWithMangaResponse(CategoryProjection category, Page<MangaDtoRandom> mangas) {
    this.category = category;
    this.mangas = mangas;
}

public CategoryProjection getCategory() {
    return category;
}

public void setCategory(CategoryProjection category) {
    this.category = category;
}

public Page<MangaDtoRandom> getMangas() {
    return mangas;
}

public void setMangas(Page<MangaDtoRandom> mangas) {
    this.mangas = mangas;
}
}