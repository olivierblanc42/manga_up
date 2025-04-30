package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import manga_up.manga_up.projection.appUser.AppUserProjection;
import manga_up.manga_up.service.UserService;
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
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER= LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "All users with pagination")
    @ApiResponse(responseCode =  "201", description = "All users have been retrieved")
    @GetMapping
    public ResponseEntity<Page<AppUserProjection>> getAllUsers(@PageableDefault(
            page = 0,
            size = 10,
            sort = "createdAt",
            direction = Sort.Direction.DESC
    ) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all addresses with pagination");
        Page<AppUserProjection> users = userService.findAllByPage(pageable);
        LOGGER.info("Found {} addresses", users.getTotalElements());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
