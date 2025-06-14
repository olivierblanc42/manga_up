package manga_up.manga_up.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.CategoryDao;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryDto;
import manga_up.manga_up.dto.category.CategoryWithMangaResponse;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.service.CategoryService;
import manga_up.manga_up.service.CustomUserDetailsService;

@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CategoryDao categoryDao;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    private static class TestCategoryProjection implements CategoryProjection {
        private final Integer id;
        private final String label;
        private final LocalDateTime createdAt;
        private final String description;
        private final String url;

        public TestCategoryProjection(Integer id, String label, LocalDateTime createdAt, String description,
                String url) {
            this.id = id;
            this.label = label;
            this.createdAt = createdAt;
            this.description = description;
            this.url = url;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnCategoryWithmanga() throws Exception {
        CategoryProjection category = new TestCategoryProjection(
                1,
                "Aventure",
                LocalDateTime.of(2023, 6, 10, 14, 0),
                "Catégorie regroupant les œuvres pleines d’action et de voyages.",
                "https://example.com/category/aventure");

        Set<AuthorDtoRandom> authorsSet = new HashSet<>();
        authorsSet.add(new AuthorDtoRandom(1, "Akira", "Toriyama"));
        authorsSet.add(new AuthorDtoRandom(2, "Naoko", "Takeuchi"));

        MangaDtoRandom manga1 = new MangaDtoRandom(101, "Dragon Ball", authorsSet, "url1");
        MangaDtoRandom manga2 = new MangaDtoRandom(102, "Sailor Moon", authorsSet, "url2");

        List<MangaDtoRandom> mangasList = List.of(manga1, manga2);
        Page<MangaDtoRandom> mangasPage = new PageImpl<>(mangasList);

        CategoryWithMangaResponse response = new CategoryWithMangaResponse(category, mangasPage);
        when(categoryService.getCategoryWithMangas(any(Integer.class), any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/api/categories/category/1/mangas").with(csrf())
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category.label").value("Aventure"))
                .andExpect(jsonPath("$.mangas.content.length()").value(2))
                .andExpect(jsonPath("$.mangas.content[0].title").value("Dragon Ball"))
                .andExpect(jsonPath("$.mangas.content[1].title").value("Sailor Moon"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnCreatedCategory() throws Exception {
        String json = """
                    {
                        "id": 1,
                        "label": "string",
                        "description": "string",
                        "url": "string"
                    }
                """;

        CategoryDto a = new CategoryDto(1, "string", "string", "string");

        when(categoryService.save(any())).thenReturn(a);

        mockMvc.perform(post("/api/categories/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label").value("string"));
    }



    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnUpdateCategory() throws Exception {
        String json = """
                    {
                        "id": 1,
                        "label": "string",
                        "description": "string",
                        "url": "string"
                    }
                """;

        CategoryDto categoryUpdate = new CategoryDto(1, "string", "string", "string.com");

    when(categoryService.update(eq(1), any(CategoryDto.class))).thenReturn(categoryUpdate);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value("string.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnInternalServerErrorWhenUpdateFails() throws Exception {
        String json = """
                    {
                        "id": 1,
                        "label": "string",
                        "description": "string",
                        "url": "string"
                    }
                """;

        when(categoryService.update(eq(1), any(CategoryDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategoryById(1);
    }

}
