package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dto.CategoryDto;
import manga_up.manga_up.model.Category;
import manga_up.manga_up.projection.CategoryProjection;
import manga_up.manga_up.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private static final Logger LOGGER= LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;
    private final CategoryDao categoryDao;

    public CategoryController(CategoryService categoryService, CategoryDao categoryDao) {
        this.categoryService = categoryService;
        this.categoryDao = categoryDao;
    }

    @Operation(summary = "All Categories with pagination")
    @ApiResponse(responseCode =  "201", description = "All Categories have been retrieved")
    @GetMapping
    public ResponseEntity<Page<CategoryProjection>> getCategories(
            @PageableDefault(
            page = 0,
            size = 10,
            sort = "createdAt",
            direction = Sort.Direction.DESC
    ) @ParameterObject Pageable pageable)
    {
        LOGGER.info("Find all Categories with pagination");
        Page<CategoryProjection> categories = categoryService.FindAllCategorisByPage(pageable);
        LOGGER.info("Found {} addresses", categories.getTotalElements());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Find category by id ")
    @GetMapping("{id}")
    public ResponseEntity<CategoryProjection> getCategory(@PathVariable Integer id) {
        LOGGER.info("Find category by id {}", id);
        return ResponseEntity.ok(categoryService.FindCategoryById(id));
    }


    @Operation(summary = "delete category by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "400", description = "Category used by users"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        LOGGER.info("Deleting address by id");
        categoryService.deleteCategoryById(id);
    }

    @Operation(summary = "Add category")
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto category) {
        LOGGER.info("Add category: {}", category);
        return ResponseEntity.ok(categoryService.save(category));
    }

    @Operation(summary = "Update Category")
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer id, CategoryDto categoryDto) {
        LOGGER.info("Update category: {}", categoryDto);
       try{
           CategoryDto category = categoryService.update( id , categoryDto );
           return new ResponseEntity<>(category, HttpStatus.OK);

       }catch (Exception e){
           LOGGER.error("Error updating category", e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
       }
    }

}
