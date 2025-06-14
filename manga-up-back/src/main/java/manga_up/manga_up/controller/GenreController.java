package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.projection.genre.GenreProjection;
import manga_up.manga_up.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreController.class);

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;

    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Find genre with id")
    @GetMapping("{id}")
    public ResponseEntity<GenreProjection> getGenreById(@PathVariable Integer id) {
        LOGGER.info("Find genre with id");
        return ResponseEntity.ok(genreService.findGenreUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete genre by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre deleted"),
            @ApiResponse(responseCode = "400", description = "Genre used by users"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @DeleteMapping("{id}")
    public void deleteGenreById(@PathVariable Integer id) {
        LOGGER.info("Delete genre with id");
        genreService.deleteGenre(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Adding a gender")
    @PostMapping("/add")
    public ResponseEntity<?> addGenre(@RequestBody GenreDto genre) {
        LOGGER.info("Adding a genre");
        return ResponseEntity.ok(genreService.save(genre));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update manga genre")
    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable Integer id, @RequestBody GenreDto genreDto) {
        LOGGER.info("Update manga genre");
        try {
            GenreDto genre = genreService.updateGenre(id, genreDto);
            return new ResponseEntity<>(genre, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error updating genre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

 

}
