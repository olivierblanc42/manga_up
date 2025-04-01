package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import manga_up.manga_up.dto.MangaDto;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.MangaProjection;
import manga_up.manga_up.service.MangaService;
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
@RequestMapping("/api/mangas")
public class MangaController {
    private static final Logger LOGGER= LoggerFactory.getLogger(MangaController.class);
    private final MangaService mangaService;


    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }

    @Operation(summary = "All Mangas with pagination")
    @ApiResponse(responseCode =  "201", description = "All manga have been retrieved")
    @GetMapping
    public ResponseEntity<Page<MangaProjection>> getAllManga(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all addresses with pagination");
        Page<MangaProjection> mangas = mangaService.findAllByPage(pageable);
        LOGGER.info("Found {} addresses", mangas.getTotalElements());
        return new ResponseEntity<>(mangas, HttpStatus.OK);
    }


    @Operation(summary = "Adding Manga")
    @PostMapping("/add")
    public ResponseEntity<MangaDto> addManga(@Valid @RequestBody MangaDto mangaDto) {
        LOGGER.info("Adding Manga");
        return  ResponseEntity.ok(mangaService.save(mangaDto));
    }
}
