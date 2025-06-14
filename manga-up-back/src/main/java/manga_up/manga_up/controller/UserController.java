package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import manga_up.manga_up.dto.appUser.UserProfilDto;

import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.projection.appUser.AppUserProjection;
import manga_up.manga_up.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
  

    public UserController(UserService userService) {
        this.userService = userService;
      
    }

    @PreAuthorize("hasRole('ADMIN')")  
    @Operation(summary = "All users with pagination")
    @ApiResponse(responseCode = "201", description = "All users have been retrieved")
    @GetMapping
    public ResponseEntity<Page<AppUserProjection>> getAllUsers(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all addresses with pagination");
        Page<AppUserProjection> users = userService.findAllByPage(pageable);
        LOGGER.info("Found {} addresses", users.getTotalElements());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfilDto> getCurrentUser() {
        return userService.getCurrentUser();
    }




@PostMapping("/manga/{mangaId}")
public ResponseEntity<?> addFavorite(@PathVariable Integer mangaId) {
    try {
        AppUser user = userService.getAuthenticatedUserEntity();
        userService.addFavorite(user.getId(), mangaId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Manga ajouté aux favoris avec succès.");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erreur lors de l'ajout aux favoris.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}

@DeleteMapping("/manga/{mangaId}")
public ResponseEntity<?> removeFavorite(@PathVariable Integer mangaId) {
    try {
        AppUser user = userService.getAuthenticatedUserEntity();
        userService.removeFavorite(user.getId(), mangaId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Manga supprimé des favoris avec succès.");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erreur lors de la suppression des favoris.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}  

    @GetMapping("/manga/{mangaId}/is-favorite")
    public ResponseEntity<?> isFavorite(@PathVariable Integer mangaId) {
        try {
            LOGGER.info("[CONTROLLER] Vérification si le manga ID={} est en favori", mangaId);

            AppUser user = userService.getAuthenticatedUserEntity();
            boolean isFavorite = userService.isFavorite(user.getId(), mangaId);
            LOGGER.info("[CONTROLLER] Résultat : isFavorite = {}", isFavorite);

            return ResponseEntity.ok(Boolean.valueOf(isFavorite));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utilisateur non authentifié");
        }
    }
    
}
