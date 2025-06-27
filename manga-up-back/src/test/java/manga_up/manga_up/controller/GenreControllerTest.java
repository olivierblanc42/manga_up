package manga_up.manga_up.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dao.GenreDao;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.mapper.AuthorMapper;
import manga_up.manga_up.mapper.CategoryMapper;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.projection.genre.GenreProjection;
import manga_up.manga_up.service.AuthorService;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.GenreService;

@WebMvcTest(GenreController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GenreControllerTest {
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;
    @MockitoBean
    private GenreDao GenreDao;
    @Mock
    private CategoryMapper CategoryMapper;

    private static class TestGenreProjection implements GenreProjection {
        private final Integer id;
        private final String label;
        private final String url;
        private final String description;
        private final LocalDateTime createdAt;

        public TestGenreProjection(Integer id, String label, String url, String description, LocalDateTime createdAt) {
            this.id = id;
            this.label = label;
            this.url = url;
            this.description = description;
            this.createdAt = createdAt;
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
        public String getUrl() {
            return url;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnGenreByid() throws Exception {
        TestGenreProjection genre = new TestGenreProjection(
                1,
                "Action",
                "/action",
                "Genre focused on action-packed stories",
                LocalDateTime.of(2023, 1, 10, 12, 0));

        when(genreService.findGenreUserById(1)).thenReturn(genre);
        mockMvc.perform(get("/api/genres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Action"))
                .andExpect(jsonPath("$.url").value("/action"));
    }
    
   @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnCreatedGenre() throws Exception {
        String json = """
                    {
                        "label": "Toriyama",
                        "url": "Toriyama",
                        "description": "Mangaka japonais, créateur de Dragon Ball."

                    }
                """;

        GenreDto a = new GenreDto(1, "Toriyama", "Toriyama", "Mangaka japonais, créateur de Dragon Ball.");

        when(genreService.save(any())).thenReturn(a);

        mockMvc.perform(post("/api/genres/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label").value("Toriyama"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldUpdateCreatedGenre() throws Exception {
        String json = """
                    {
                        "label": "Toriyama",
                        "url": "Toriyama",
                        "description": "Mangaka japonais, créateur de Dragon Ball."

                    }
                """;

        GenreDto genreUpdate = new GenreDto(2, "Toriyama", "Test", "Mangaka japonais, créateur de Dragon Ball.");

        when(genreService.updateGenre(eq(1), any(GenreDto.class))).thenReturn(genreUpdate);

        mockMvc.perform(put("/api/genres/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label").value("Test"));

    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnInternalServerErrorWhenUpdateFails() throws Exception {
        String json = """
                    {
                        "label": "Toriyama",
                        "url": "Toriyama",
                        "description": "Mangaka japonais, créateur de Dragon Ball."

                    }
                """;

        when(genreService.updateGenre(eq(1), any(GenreDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/api/genres/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/genres/1").with(csrf()))
                .andExpect(status().isOk());

        verify(genreService).deleteGenre(1);
        ;
    }

}
