package manga_up.manga_up.service;

import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.mapper.CategoryMapper;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.category.CategoryProjection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
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
        private final String description;
        private final String url;

        private TestCategoryProjection(Integer id, String label, LocalDateTime createdAt, String description,
                String url) {
            this.id = id;
            this.label = label;
            this.createdAt = createdAt;
            this.description = description;
            this.url = url;
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

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getUrl() {
            return url;
        }

    }

    @Test
    void shouldReturnAllCategories() {
        Pageable pageable = PageRequest.of(0, 5);

        CategoryProjection c1 = new TestCategoryProjection(
                1,
                "Baston",
                LocalDateTime.of(2023, 5, 12, 14, 30),
                "descritpion",
                "com.com");
        CategoryProjection c2 = new TestCategoryProjection(
                2,
                "Comédie",
                LocalDateTime.of(2023, 5, 12, 14, 30),
                "descritpion",
                "com.com");
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
                LocalDateTime.of(2023, 5, 12, 14, 30),
                "descritpion",
                "com.com");
        when(categoryDao.findCategoryProjectionById(1)).thenReturn(Optional.of(c1));
        CategoryProjection categoryProjection = categoryService.findCategoryById(1);

        assertThat(categoryProjection).isEqualTo(c1);
    }

    @Test
    void shouldReturnCategorySave() {
        CategoryDto categoryDto = new CategoryDto(
                1,
                "Baston",
                 "description",
                "test.com");
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
        int id = 1;
        CategoryDto categoryDto = new CategoryDto(
                id,
                "Baston",
                "description",
                "test.com");

        Category categoryEntity = new Category();
        categoryEntity.setId(id);
        categoryEntity.setLabel("old");
        categoryEntity.setDescription("old description");
        categoryEntity.setCreatedAt(Instant.now());

        when(categoryDao.findCategoryById(id)).thenReturn(Optional.of(categoryEntity));
        when(categoryDao.save(categoryEntity)).thenReturn(categoryEntity);
        when(categoryMapper.toDtoCategory(categoryEntity)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(id, categoryDto);

        assertThat(result).isNotNull();
        assertThat(result.getLabel()).isEqualTo("Baston");
        assertThat(result.getDescription()).isEqualTo("description");
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