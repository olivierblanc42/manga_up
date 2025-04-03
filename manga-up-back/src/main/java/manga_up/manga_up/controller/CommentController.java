package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import manga_up.manga_up.projection.CommentProjection;
import manga_up.manga_up.projection.UserAddressProjection;
import manga_up.manga_up.service.CommentService;
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
@RequestMapping("/api/comments")
public class CommentController {
    private static final Logger LOGGER= LoggerFactory.getLogger(CommentController .class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "All comments with the pagination")
    @GetMapping()
    public ResponseEntity<Page<CommentProjection>> findAllByPage(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all comments with the pagination");
        Page<CommentProjection> comment = commentService.getAllComment(pageable);
        LOGGER.info("Found {} addresses", comment.getTotalElements());
        return new  ResponseEntity<>(comment, HttpStatus.OK);
    }
}
