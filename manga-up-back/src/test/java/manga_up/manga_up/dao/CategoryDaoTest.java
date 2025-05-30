package manga_up.manga_up.dao;

import manga_up.manga_up.model.Author;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.category.CategoryProjection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoryDaoTest {

    @Autowired
    private CategoryDao categoryDao;
  @Test
    void getAllCategories() {
      List<Category> categories = categoryDao.findAll();

      assertEquals(2, categories.size());
      assertEquals("Action", categories.get(0).getLabel());
  }


  @Test
    void getCategoryById() {
      Category category = categoryDao.findById(1).get();
      assertEquals("Action", category.getLabel());
  }

  @Test
    void shouldSaveCategory() {
      Category category = new Category();
      category.setLabel("Baston");
      category.setDescription("Description");
      category.setCreatedAt(Instant.parse("2023-04-10T10:00:00Z"));
      Category saveCategory = categoryDao.save(category);
      assertNotNull(saveCategory.getId());
      assertEquals("Baston", saveCategory.getLabel());
      assertEquals("Description", saveCategory.getDescription());
      assertEquals(Instant.parse("2023-04-10T10:00:00Z"), saveCategory.getCreatedAt());
  }

  @Test
    void shouldUpdateCategory() {
      Category category = categoryDao.findById(1).get();
      category.setDescription("New Description");

      Category saveCategory = categoryDao.save(category);
      assertEquals("New Description", saveCategory.getDescription());
  }

  @Test
    void shouldDeleteCategory() {
      categoryDao.delete(categoryDao.findById(1).get());

      Optional<Category> categoryOptional = categoryDao.findById(1);
      assertFalse(categoryOptional.isPresent());
  }


@Test
void shouldReturnPagedCategoryProjection() {
    Pageable pageable = PageRequest.of(0, 2);
    Page<CategoryProjection> page = categoryDao.findAllCategorisByPage(pageable);

    assertEquals(2, page.getTotalElements());
    assertEquals("Action", page.getContent().get(0).getLabel());
}

@Test
void shouldFindCategoryWithMangas() {
  Optional<Category> optionalCategory = categoryDao.findCategoryById(1);
  assertTrue(optionalCategory.isPresent());
  assertEquals("Action", optionalCategory.get().getLabel());
}

@Test
void shouldReturnCategoryProjectionById() {
  Optional<CategoryProjection> projection = categoryDao.findCategoryProjectionById(1);
  assertTrue(projection.isPresent());
  assertEquals("Action", projection.get().getLabel());
}

}