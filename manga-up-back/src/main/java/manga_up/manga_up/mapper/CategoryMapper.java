package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.model.Category;

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
        category.setLabel(categoryDto.getLabel());
        category.setDescription(categoryDto.getDescription());
        category.setUrl(categoryDto.getUrl());
        return category;
    }




 public CategoryLittleDto toLittleDtoCategory(Category category) {
        return new CategoryLittleDto(
                category.getId(),
                category.getLabel()
        );
 }




 public Category categoryLittleDto(CategoryLittleDto categoryLittleDto) {
        Category category = new Category();
        category.setId(categoryLittleDto.getId());
        category.setLabel(categoryLittleDto.getLabel());
        return category;
 }

}
