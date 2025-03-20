package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import manga_up.manga_up.model.Address;
import manga_up.manga_up.model.Picture;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/picture")
public class PictureController {
    private static final Logger LOGGER= LoggerFactory.getLogger(PictureController.class);

    private PictureService pictureService;
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @Operation(summary = "All pictures with pagination")
    @ApiResponse(responseCode = "201", description = "All pictures have been retrived")
    @GetMapping
    public ResponseEntity<Page<Picture>> getAllPicture( @PageableDefault(
            page = 0,
            size = 10,
            sort = "createdAt",
            direction = Sort.Direction.DESC
    ) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all pictures with pagination");
        Page<Picture> pictures = pictureService.findAllByPage(pageable);
        LOGGER.info("Found {} pictures", pictures.getTotalElements());
        return new ResponseEntity<>(pictures, HttpStatus.OK);


    }

}
