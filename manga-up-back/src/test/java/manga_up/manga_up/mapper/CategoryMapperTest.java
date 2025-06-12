package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryLittleDto;
import manga_up.manga_up.dto.status.StatusDto;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.model.Status;

@ActiveProfiles("test")
public class CategoryMapperTest {


    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapper();
    }




    @Test
    void shouldTotoDtoCategory() {
        Category category = new Category();
        category.setId(1);
        category.setLabel("Combat");
        category.setDescription("le combat");
        category.setUrl(".com");

        CategoryDto categoryDto = categoryMapper.toDtoCategory(category);

        assertNotNull(categoryDto);
        assertEquals(1, categoryDto.getId());
        assertEquals("Combat", categoryDto.getLabel());

    }

    @Test
    void shouldtoEntity() {

        CategoryDto categoryDto = new CategoryDto(1, "Combat","le combat",".com");

        Category category = categoryMapper.toEntity(categoryDto);

     assertNotNull(category);
     assertEquals(1, category.getId());
     assertEquals("Combat", category.getLabel());

    }


    @Test
    void shoulDtoLittleDtoCategory() {
        Category category = new Category();
        category.setId(1);


        CategoryLittleDto toLittleDtoCategory = categoryMapper.toLittleDtoCategory(category);

        assertNotNull(toLittleDtoCategory);
        assertEquals(1, toLittleDtoCategory.getId());

    }

    @Test
    void shouldtoCategoryLitleEntity() {

        CategoryLittleDto categoryLittleDto = new CategoryLittleDto(1);

        Category category = categoryMapper.categoryLittleDto(categoryLittleDto);

        assertNotNull(category);
        assertEquals(1, category.getId());

    }

}
