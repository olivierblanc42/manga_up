package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import manga_up.manga_up.dto.MangaDto;
import manga_up.manga_up.dto.MangaDtoRandom;
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

import java.util.List;
@Tag(name = "2.Mangas", description = "Operations related to mangas")

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
    @GetMapping()
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

@Operation(summary ="Get one manga with her id")
@GetMapping("{id}")
public ResponseEntity<MangaProjection> getMangaById(@PathVariable Integer id) {
        LOGGER.info("Find manga by id");
        return  ResponseEntity.ok(mangaService.findMangaById(id));
}

  @Operation(summary ="Get Random Four Mangas")
   @GetMapping("/four")
     public ResponseEntity<List<MangaDtoRandom>> getRandomFourMangas(){
        LOGGER.info("Get six Mangas");
        List<MangaDtoRandom> mangas = mangaService.getRandomMangas();
       LOGGER.info("Found {} mangas", mangas.size());
        return new ResponseEntity<>(mangas, HttpStatus.OK);
    }

    @Operation(summary ="Get Random Manga")
    @GetMapping("/one")
    public ResponseEntity<List<MangaDtoRandom>> getRandomManga(){
       LOGGER.info("Get  Manga");
        List<MangaDtoRandom> mangas = mangaService.getRandomManga();
       return new ResponseEntity<>(mangas, HttpStatus.OK);
    }

    @Operation(summary = "Adding Manga")
    @PostMapping("/add")
    public ResponseEntity<MangaDto> addManga(@Valid @RequestBody MangaDto mangaDto) {
        LOGGER.info("Adding Manga");
        return  ResponseEntity.ok(mangaService.save(mangaDto));
    }

    @Operation(summary = "delete manga")
    @DeleteMapping("/{id}")
    public void deleteManga( Integer mangaId) {
        LOGGER.info("Deleting manga by id");
        mangaService.deleteManga( mangaId);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MangaDto> updateManga(@PathVariable Integer id, @RequestBody MangaDto mangaUpdateDto) {
        try{
            MangaDto updateManga = mangaService.updateManga(id ,mangaUpdateDto) ;
            return ResponseEntity.ok(updateManga);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
