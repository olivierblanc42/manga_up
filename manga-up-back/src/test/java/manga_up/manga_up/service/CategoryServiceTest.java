package manga_up.manga_up.service;

import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dto.CategoryDto;
import manga_up.manga_up.mapper.CategoryMapper;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.CategoryProjection;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CategoryServiceTest {
    @Mock
    private CategoryDao categoryDao;
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryMapper categoryMapper;



    private static class TestCategoryProjection implements CategoryProjection {
        private final Integer id;
        private final String label;
        private final LocalDateTime createdAt;

        private TestCategoryProjection(Integer id, String label, LocalDateTime createdAt) {
            this.id = id;
            this.label = label;
            this.createdAt = createdAt;
           
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

   
    }





    @Test
    void shouldReturnAllCategories() {
        Pageable pageable = PageRequest.of(0, 5);

        CategoryProjection c1 = new TestCategoryProjection(
                1,
                "Baston",
                LocalDateTime.of(2023, 5, 12, 14, 30)
        );
        CategoryProjection c2 = new TestCategoryProjection(
                2,
                "Comédie",
                LocalDateTime.of(2023, 5, 12, 14, 30)
        );
        Page<CategoryProjection> page = new PageImpl<>(List.of(c1, c2));
        when(categoryDao.findAllCategorisByPage(pageable)).thenReturn(page);
        Page<CategoryProjection> result = categoryService.findAllCategorisByPage(pageable);

        assertThat(result).hasSize(2).containsExactly(c1, c2);
    }

    @Test
    void shouldReturnCategoryById() {
        CategoryProjection c1 = new TestCategoryProjection(
                1,
                "Baston",
                LocalDateTime.of(2023, 5, 12, 14, 30)
        );
        when(categoryDao.findCategoryProjectionById(1)).thenReturn(Optional.of(c1));
        CategoryProjection categoryProjection = categoryService.findCategoryById(1);

        assertThat(categoryProjection).isEqualTo(c1);
    }

    @Test
    void shouldReturnCategorySave() {
        CategoryDto categoryDto = new CategoryDto(
                "test",
                "test"
        );
        Category categoryEntity = new Category();
        categoryEntity.setLabel("test");
        categoryEntity.setDescription("test");
        categoryEntity.setCreatedAt(Instant.now());

        when(categoryMapper.toEntity(categoryDto)).thenReturn(categoryEntity);
        when(categoryDao.save(categoryEntity)).thenReturn(categoryEntity);
        when(categoryMapper.toDtoCategory(categoryEntity)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(categoryDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(categoryDto);
    }

    @Test
    void shouldReturnCategoryUpdate() {
        // Arrange
        int id = 1;
        CategoryDto categoryDto = new CategoryDto("test1", "test123");

        Category categoryEntity = new Category();
        categoryEntity.setId(id);
        categoryEntity.setLabel("test");
        categoryEntity.setDescription("test");
        categoryEntity.setCreatedAt(Instant.now());

        when(categoryDao.findCategoryById(id)).thenReturn(Optional.of(categoryEntity));
        categoryEntity.setLabel(categoryDto.getLabel());
        categoryEntity.setDescription(categoryDto.getDescription());
        when(categoryDao.save(categoryEntity)).thenReturn(categoryEntity);
        when(categoryMapper.toDtoCategory(categoryEntity)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.update(id, categoryDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getLabel()).isEqualTo("test1");
        assertThat(result.getDescription()).isEqualTo("test123");


    }

    @Test
    void shouldDeleteCategory() {
        Category category = new Category();
        category.setId(1);
        category.setLabel("test");
        category.setDescription("test");

        when(categoryDao.findCategoryById(1)).thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(1);

        verify(categoryDao).delete(category);

    }

}