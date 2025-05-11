package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import manga_up.manga_up.dao.UserDao;
import manga_up.manga_up.dto.appUser.UserProfilDto;
import manga_up.manga_up.dto.appUser.UserResponseDto;
import manga_up.manga_up.mapper.AppUserMapper;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.projection.appUser.AppUserProjection;
import manga_up.manga_up.service.UserService;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserDao userDao;
    private final AppUserMapper userMapper;

    public UserController(UserService userService, UserDao userDao, AppUserMapper userMapper) {
        this.userService = userService;
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

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
            // Récupérer l'entité AppUser pour manipuler les données en backend
            AppUser user = userService.getAuthenticatedUserEntity();

            // Appel au service pour ajouter le manga aux favoris
            userService.addFavorite(user.getId(), mangaId);

            return ResponseEntity.ok("Manga ajouté aux favoris avec succès."); 
        } catch (Exception e) {
            // Si une exception se produit (par exemple si l'utilisateur n'est pas
            // authentifié)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erreur lors de l'ajout aux favoris.");
        }
    }

    @DeleteMapping("/manga/{mangaId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Integer mangaId) {
        try {
            // Récupérer l'entité AppUser pour manipuler les données en backend
            AppUser user = userService.getAuthenticatedUserEntity();

            // Appel au service pour supprimer le manga des favoris
            userService.removeFavorite(user.getId(), mangaId);

            return ResponseEntity.ok("Manga supprimé des favoris avec succès."); 
        } catch (Exception e) {
            // Si une exception se produit (par exemple si l'utilisateur n'est pas
            // authentifié)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erreur lors de la suppression des favoris.");
        }
    }    


}
