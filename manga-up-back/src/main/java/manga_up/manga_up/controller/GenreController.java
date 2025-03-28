package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import manga_up.manga_up.dto.GenreDto;
import manga_up.manga_up.model.Genre;
import manga_up.manga_up.projection.GenreProjection;
import manga_up.manga_up.service.GenreService;
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
@RequestMapping("/api/genres")
public class GenreController {
    private static final Logger LOGGER= LoggerFactory.getLogger(GenreController.class);

    private final GenreService genreService;
    public GenreController(GenreService genreService) {
        this.genreService = genreService;

    }
    @Operation(summary = "All genres with pagination")
    @ApiResponse(responseCode =  "201", description = "All genres have been retrieved")
    @GetMapping
    public ResponseEntity<Page<GenreProjection>> getAllGenres(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all genres with pagination");
        Page<GenreProjection> genres = genreService.findAllByGenre(pageable);
        LOGGER.info("Found {} genres", genres.getTotalElements());
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }



    @Operation(summary = "Adding a gender")
    @PostMapping("/add")
    public ResponseEntity<?> addGenre(@RequestBody GenreDto genre) {
        LOGGER.info("Adding a genre");
        return ResponseEntity.ok(genreService.save(genre));
    }

}
