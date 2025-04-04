package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import manga_up.manga_up.dto.GenderUserDto;
import manga_up.manga_up.projection.GenderUserProjection;
import manga_up.manga_up.service.GenreUserService;
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
@RequestMapping("/api/gendersUser")
public class GenderUserController {

    private static final Logger LOGGER= LoggerFactory.getLogger(GenderUserController .class);


    private final GenreUserService genreUserService;

    public GenderUserController(GenreUserService genreUserService) {
        this.genreUserService = genreUserService;
    }

    @Operation(summary = "All genreUsers with pagination")
    @GetMapping
    public ResponseEntity<Page<GenderUserProjection>> getAllGenreUsers(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "label",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Getting genreUsers");
        Page<GenderUserProjection> genderUserProjections = genreUserService.getGenreUsers(pageable);
        return new ResponseEntity<>(genderUserProjections, HttpStatus.OK);
    }


    @Operation(summary = "Add one genre User")
    @PostMapping("add")
    public ResponseEntity<GenderUserDto> addGenreUser(@RequestBody GenderUserDto genreUserDto) {
        LOGGER.info("Adding genreUser");
        return ResponseEntity.ok(genreUserService.saveGenreUser(genreUserDto));
    }


    @Operation(summary = "update Gender user")
    @PutMapping("/update")
    public ResponseEntity<GenderUserDto> updateGenreUser(@ParameterObject Integer id,   @RequestBody GenderUserDto genreUserDto) {
        LOGGER.info("Updating genreUser");
        try{
            GenderUserDto genreUser = genreUserService.updateGenreUser(id, genreUserDto);
            return new ResponseEntity<>(genreUser, HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("Error updating gender user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
