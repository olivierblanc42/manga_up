package manga_up.manga_up.dto.category;

import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.projection.manga.MangaBaseProjection;

import org.springframework.data.domain.Page;

public class CategoryWithMangaResponse {

private CategoryProjection category;
private Page<MangaBaseProjection> mangas;

public CategoryWithMangaResponse(CategoryProjection category, Page<MangaBaseProjection> mangas) {
    this.category = category;
    this.mangas = mangas;
}

public CategoryProjection getCategory() {
    return category;
}

public void setCategory(CategoryProjection category) {
    this.category = category;
}

public Page<MangaBaseProjection> getMangas() {
    return mangas;
}

public void setMangas(Page<MangaBaseProjection> mangas) {
    this.mangas = mangas;
}
}