package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryWithMangaResponse;
import manga_up.manga_up.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private static final Logger LOGGER= LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService, CategoryDao categoryDao) {
        this.categoryService = categoryService;
    }

 

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete category by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "400", description = "Category used by users"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        LOGGER.info("Deleting address by id");
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add category")
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto category) {
        LOGGER.info("Add category: {}", category);
        return ResponseEntity.ok(categoryService.save(category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Integer id,
            @RequestBody CategoryDto categoryDto) {
        LOGGER.info("Update category: {}", categoryDto);
        System.out.println("Reçu : " + categoryDto);
        try {
            CategoryDto category = categoryService.update(id, categoryDto);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error updating category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }    

    @Operation(summary = "Get category with mangas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author with mangas retrieved"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("category/{categoryId}/mangas")
    public CategoryWithMangaResponse getCategoryWithMangas(@PathVariable Integer categoryId,
            @PageableDefault(page = 0, size = 8, sort = "title", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        return categoryService.getCategoryWithMangas(categoryId, pageable);
    }

}
