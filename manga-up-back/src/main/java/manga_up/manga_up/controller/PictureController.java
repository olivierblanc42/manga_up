package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import manga_up.manga_up.dto.picture.PictureDto;
import manga_up.manga_up.model.Picture;
import manga_up.manga_up.projection.pictureProjection.PictureProjection;
import manga_up.manga_up.service.PictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "3.Images", description = "Operations related to manga pictures")
@RestController
@RequestMapping("/api/picture")
public class PictureController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PictureController.class);

    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "All pictures with pagination")
    @ApiResponse(responseCode = "201", description = "All pictures have been retrived")
    @GetMapping
    public ResponseEntity<Page<PictureProjection>> getAllPicture(
            @PageableDefault(page = 0, size = 10, sort = "url", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all pictures with pagination");
        Page<PictureProjection> pictures = pictureService.findAllByPage(pageable);
        LOGGER.info("Found {} pictures", pictures.getTotalElements());
        return new ResponseEntity<>(pictures, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get picture with her id")
    @GetMapping("{id}")
    public ResponseEntity<PictureProjection> findById(@PathVariable Integer id) {
        LOGGER.info("Find picture with id {}", id);
        PictureProjection pictureProjection = pictureService.findById(id);
        LOGGER.info("Found picture with id {}", id);
        return new ResponseEntity<>(pictureProjection, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")

    @Operation(summary = "Update url picture manga")
    @PutMapping("{id}")
    ResponseEntity<PictureDto> updatePicture(@PathVariable Integer id, @RequestBody PictureDto pictureDto) {
        LOGGER.info("Updating picture");
        try {
            PictureDto picture = pictureService.UpdatePicture(id, pictureDto);
            return new ResponseEntity<>(picture, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error updating picture", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
