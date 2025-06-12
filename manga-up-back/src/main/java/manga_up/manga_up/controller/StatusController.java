package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import manga_up.manga_up.projection.status.StatusProjection;
import manga_up.manga_up.service.StatusService;
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
@RequestMapping("/api/status")
public class StatusController {
    private static final Logger LOGGER= LoggerFactory.getLogger(StatusController .class);

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }


    @Operation(summary = "All Status with the pagination")
    @GetMapping()
    public ResponseEntity<Page<StatusProjection>> findAllByPage(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all comments with the pagination");
        Page<StatusProjection> status = statusService.findAllByPage(pageable);
        LOGGER.info("Found {} addresses", status.getTotalElements());
        return new  ResponseEntity<>(status, HttpStatus.OK);
    }
}
