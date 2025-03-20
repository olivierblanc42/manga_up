package manga_up.manga_up.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.model.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private static final Logger LOGGER= LoggerFactory.getLogger(AuthorController .class);

    private final AuthorDao authorDao;


    public AuthorController(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Operation(summary = "All Authors with pagination")
    @ApiResponse(responseCode =  "201", description = "All Authors have been retrived")
    @GetMapping
    public ResponseEntity<Page<Author>> getAllAuthors(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all Authors with pagination");
        Page<Author> authors = authorDao.findAll(pageable);
        LOGGER.info("Found {} addresses", authors.getTotalElements());
        return new ResponseEntity<>(authors, HttpStatus.OK);

    }
}
