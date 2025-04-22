package manga_up.manga_up.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import manga_up.manga_up.dto.AuthorDto;
import manga_up.manga_up.dto.AuthorWithMangasResponse;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.projection.AuthorProjection;
import manga_up.manga_up.service.AuthorService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private static final Logger LOGGER= LoggerFactory.getLogger(AuthorController .class);

    private final AuthorService authorService;

    public AuthorController(AuthorDao authorDao, AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "All Authors with pagination")
    @ApiResponse(responseCode =  "201", description = "All Authors have been retrived")
    @GetMapping("pagination")
    public ResponseEntity<Page<AuthorProjection>> getAllAuthors(
            @PageableDefault(
                    page = 0,
                    size = 8,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all Authors with pagination");
        Page<AuthorProjection> authors = authorService.getAllAuthors(pageable);
        LOGGER.info("Found {} addresses", authors.getTotalElements());
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }


    @Operation(summary = "Find author ")
    @GetMapping("{id}")
    public ResponseEntity<?> getAuthor(@PathVariable Integer id) {
        LOGGER.info("Find author with id {}", id);
      return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "delete Author by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted"),
            @ApiResponse(responseCode = "400", description = "Author used by users"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @DeleteMapping("{id}")
    public void deleteAuthor(@PathVariable Integer id) {
        LOGGER.info("Delete author with id {}", id);
        authorService.deleteAuthorById(id);
    }

  @Operation(summary = "Adding Author")
  @PostMapping("/add")
  public ResponseEntity<AuthorDto> addAuthor(@RequestBody AuthorDto authorDto) {
        LOGGER.info("Adding Author");
        return ResponseEntity.ok(authorService.save(authorDto));
  }



  @Operation(summary = "Update author")
  @PutMapping("/{id}")
  public ResponseEntity<AuthorDto> updateAuthor(@RequestBody AuthorDto authorDto, @PathVariable Integer id) {
        LOGGER.info("Updating Author");
        try {
            AuthorDto author = authorService.updateAuthor(id, authorDto);
            return new ResponseEntity<>(author, HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error("Error updating Author", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
  }
    @GetMapping("/authors/{authorId}/mangas")
    public AuthorWithMangasResponse getAuthorWithMangas
    (@PathVariable Integer authorId,  
            @PageableDefault(page = 0, size = 8, sort = "title", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) { 
        return authorService.getAuthorWithMangas(authorId, pageable);  
    }
}
