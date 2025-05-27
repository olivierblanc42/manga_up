package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.dto.comment.CommentDto;
import manga_up.manga_up.dto.comment.CommentLightDto;
import manga_up.manga_up.projection.comment.CommentProjection;
import manga_up.manga_up.projection.userAdress.UserAddressProjection;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "All comments with the pagination")
    @GetMapping()
    public ResponseEntity<Page<CommentProjection>> findAllByPage(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all comments with the pagination");
        Page<CommentProjection> comment = commentService.getAllComment(pageable);
        LOGGER.info("Found {} addresses", comment.getTotalElements());
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @Operation(summary = " One comment")
    @GetMapping("{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Integer id) {
        LOGGER.info("Find comment by id: {}", id);
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @PreAuthorize("hasRole('ADMIN')")

    @Operation(summary = "Update Comment")
    @PutMapping("{id}")
    public ResponseEntity<CommentLightDto> updateAddress(@PathVariable Integer id,
            @RequestBody CommentLightDto commentLightDto) {
        LOGGER.info("Updating address");
        try {
            CommentLightDto commentDto = commentService.updateComment(id, commentLightDto);
            return new ResponseEntity<>(commentDto, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error updating Comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")

    @Operation(summary = "delete comment by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted"),
            @ApiResponse(responseCode = "400", description = "Comment used by users"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Integer id) {
        LOGGER.info("Deleting address by id");
        commentService.deleteComment(id);
    }

}
