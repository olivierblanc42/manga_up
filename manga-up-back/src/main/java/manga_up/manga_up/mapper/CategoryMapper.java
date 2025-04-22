package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.CategoryDto;
import manga_up.manga_up.dto.CategoryLittleDto;
import manga_up.manga_up.model.Category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {


    public CategoryDto toDtoCategory(Category category) {
               return new CategoryDto(
                       category.getLabel(),
                       category.getDescription()
               );
    }

    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setLabel(categoryDto.getLabel());
        category.setDescription(categoryDto.getDescription());
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
