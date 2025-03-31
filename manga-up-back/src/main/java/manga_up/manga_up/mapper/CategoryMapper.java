package manga_up.manga_up.mapper;

import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dto.CategoryDto;
import manga_up.manga_up.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(CategoryMapper.class);

    private final CategoryDao categoryDao;

    public CategoryMapper(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

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

}
