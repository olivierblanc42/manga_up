package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.model.Category;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {


    public CategoryDto toDtoCategory(Category category) {
               return new CategoryDto(
                       category.getId(),
                       category.getLabel(),
                       category.getDescription(),
                       category.getUrl()
               );
    }

    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setLabel(Jsoup.clean(categoryDto.getLabel(), Safelist.none()));
        category.setDescription(Jsoup.clean(categoryDto.getDescription(), Safelist.none()));
        category.setUrl(Jsoup.clean(categoryDto.getUrl(), Safelist.none()));
        return category;
    }



 public CategoryLittleDto toLittleDtoCategory(Category category) {
        return new CategoryLittleDto(
                category.getId()
        );
 }




 public Category categoryLittleDto(CategoryLittleDto categoryLittleDto) {
        Category category = new Category();
        category.setId(categoryLittleDto.getId());
        return category;
 }

}
