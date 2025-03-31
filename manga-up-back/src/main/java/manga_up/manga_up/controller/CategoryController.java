package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
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

    @Operation(summary = "Add category")
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto category) {
        LOGGER.info("Add category: {}", category);
        return ResponseEntity.ok(categoryService.save(category));
    }


}
