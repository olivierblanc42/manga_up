package manga_up.manga_up.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import manga_up.manga_up.dto.author.AuthorWithMangasResponse;
import manga_up.manga_up.dto.category.CategoryWithMangaResponse;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.manga.MangaDto;
import manga_up.manga_up.dto.manga.MangaDtoOne;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.projection.genre.GenreProjection;
import manga_up.manga_up.projection.manga.MangaBaseProjection;
import manga_up.manga_up.service.AuthorService;
import manga_up.manga_up.service.CategoryService;
import manga_up.manga_up.service.GenreService;
import manga_up.manga_up.service.GenreUserService;
import manga_up.manga_up.service.MangaService;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    private static final Logger LOGGER= LoggerFactory.getLogger(PublicController .class);

        private final MangaService mangaService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CategoryService categoryService;
    private final GenreUserService genderUserService;
       public PublicController(MangaService mangaService, AuthorService authorService, GenreService genreService, CategoryService categoryService , GenreUserService genderUserService) {
        this.mangaService = mangaService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.categoryService = categoryService;
        this.genderUserService = genderUserService;
    }


// All public requests for manga

        @Operation(summary ="Get Random Manga")
    @GetMapping("/one")
    public ResponseEntity<List<MangaDtoOne>> getRandomManga(){
       LOGGER.info("Get  Manga");
        List<MangaDtoOne> mangas = mangaService.getRandomManga();
       return new ResponseEntity<>(mangas, HttpStatus.OK);
    }

  @Operation(summary ="Get Random Four Mangas")
   @GetMapping("/four")
     public ResponseEntity<List<MangaDtoRandom>> getRandomFourMangas(){
        LOGGER.info("Get six Mangas");
        List<MangaDtoRandom> mangas = mangaService.getReleaseDateRaw();
       LOGGER.info("Found {} mangas", mangas.size());
        return new ResponseEntity<>(mangas, HttpStatus.OK);
    }

    @Operation(summary = "All Mangas with pagination")
    @ApiResponse(responseCode = "201", description = "All manga have been retrieved")
    @GetMapping("randomFour")
    public ResponseEntity<List<MangaDtoRandom>> getAllTest(){
        List<MangaDtoRandom> mangas = mangaService.getFourMangaRandom();
        return new ResponseEntity<>(mangas, HttpStatus.OK);
    }

    @Operation(summary = "All Mangas with pagination")
    @ApiResponse(responseCode = "201", description = "All manga have been retrieved")
    @GetMapping("mangas/pagination")
    public ResponseEntity<Page<MangaBaseProjection>> getAllManga(
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all addresses with pagination");
        Page<MangaBaseProjection> mangas = mangaService.findAllByPage(pageable);
        LOGGER.info("Found {} addresses", mangas.getTotalElements());
        return new ResponseEntity<>(mangas, HttpStatus.OK);
    }

    @Operation(summary = "Get one manga with her id")
    @GetMapping("manga/{id}")
    public ResponseEntity<MangaDto> getMangaById(@PathVariable Integer id) {
        LOGGER.info("Find manga by id");
        return ResponseEntity.ok(mangaService.findMangaDtoById(id));
    }

 
    @Operation(summary = "All Mangas with pagination")
    @ApiResponse(responseCode = "201", description = "All manga have been retrieved")
    @GetMapping("mangas/paginations")
    public ResponseEntity<Page<MangaDtoRandom>> getAllMangaPictureTest(
            @PageableDefault(page = 0, size = 8, sort = "title", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all addresses with pagination");
        Page<MangaDtoRandom> mangas = mangaService.getPageMangas(pageable);
        LOGGER.info("Found {} addresses", mangas.getTotalElements());
        return new ResponseEntity<>(mangas, HttpStatus.OK);
    }

    // All public requests for Authors
    
    @Operation(summary = "All Authors with pagination")
    @ApiResponse(responseCode =  "201", description = "All Authors have been retrived")
    @GetMapping("authors/pagination")
    public ResponseEntity<Page<AuthorProjection>> getAllAuthors(
            @PageableDefault(
                    page = 0,
                    size = 8,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all Authors with pagination");
        Page<AuthorProjection> authors = authorService.getAllAuthors(pageable);
        LOGGER.info("Found {} addresses", authors.getTotalElements());
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }


    @Operation(summary = "Find author ")
    @GetMapping("author/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable Integer id) {
        LOGGER.info("Find author with id {}", id);
      return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "Get author with mangas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author with mangas retrieved"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("author/{authorId}/mangas")
    public AuthorWithMangasResponse getAuthorWithMangas(@PathVariable Integer authorId,  
            @PageableDefault(page = 0, size = 8, sort = "title", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) { 
        return authorService.getAuthorWithMangas(authorId, pageable);  
    }

    // All public requests for Genres

    @Operation(summary = "Get Four Random Genres")
    @GetMapping("genres/four")
    public ResponseEntity<List<GenreDto>> getRandomFourGenres() {
        LOGGER.info("Get Four Random Genres");
        List<GenreDto> genres = genreService.getRandomFourGenres();
        LOGGER.info("Found {} genres ", genres.size());
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @Operation(summary = "All genres with pagination")
    @ApiResponse(responseCode = "201", description = "All genres have been retrieved")
    @GetMapping("genres/pagination")
    public ResponseEntity<Page<GenreProjection>> getAllGenres(
            @PageableDefault(page = 0, size = 12, sort = "createdAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all genres with pagination");
        Page<GenreProjection> genres = genreService.findAllByGenre(pageable);
        LOGGER.info("Found {} genres", genres.getTotalElements());
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }


    @Operation(summary = "Get genre with mangas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre with mangas retrieved"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @GetMapping("genres/{genreId}/mangas")
    public ResponseEntity<?> getGenreWithMangas2(@PathVariable Integer genreId,
            @PageableDefault(page = 0, size = 12, sort = "title", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        LOGGER.info("Get genre with mangas");
        return ResponseEntity.ok(genreService.getGenreWithMangas(genreId, pageable));
    }

     // All public requests for GenderUser

   // @GetMapping("categories")
   @GetMapping("genderUser")
   public ResponseEntity<List<GenderUserDto>> getGenderUserDto() {
       LOGGER.info("Get GenderUserDto");
       List<GenderUserDto> genders = genderUserService.getAllGenreUsers();
       LOGGER.info("Found {} genres ", genders.size());
       return new ResponseEntity<>(genders, HttpStatus.OK);
   }


    // All public requests for Categories
    @Operation(summary = "All Categories with pagination")
    @ApiResponse(responseCode =  "201", description = "All Categories have been retrieved")
    @GetMapping("categories/pagination")
    public ResponseEntity<Page<CategoryProjection>> getCategories(
            @PageableDefault(
            page = 0,
            size = 8,
            sort = "createdAt",
            direction = Sort.Direction.DESC
    ) @ParameterObject Pageable pageable)
    {
        LOGGER.info("Find all Categories with pagination");
        Page<CategoryProjection> categories = categoryService.findAllCategorisByPage(pageable);
        LOGGER.info("Found {} categories", categories.getTotalElements());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Get category with mangas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author with mangas retrieved"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("category/{categoryId}/mangas")
    public CategoryWithMangaResponse getCategoryWithMangas(@PathVariable Integer categoryId,
            @PageableDefault(page = 0, size = 8, sort = "title", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        return categoryService.getCategoryWithMangas(categoryId, pageable);
    }



    // TEst
    @Operation(summary = "All manga Search")
    @ApiResponse(responseCode = "201", description = "All manga have been retrived")
    @GetMapping("search/{letter}")
    public ResponseEntity<Page<MangaBaseProjection>> getAllTest(@PathVariable String letter,
            @PageableDefault(page = 0, size = 8, sort = "title", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        LOGGER.info("Find all manga with pagination");
        Page<MangaBaseProjection> manga = mangaService.getTitle(letter, pageable);
        LOGGER.info("Found {} addresses", manga.getTotalElements());
        return new ResponseEntity<>(manga, HttpStatus.OK);
    }







  
}
