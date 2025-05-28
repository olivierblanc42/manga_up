package manga_up.manga_up.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.mapper.AuthorMapper;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.service.AuthorService;
import manga_up.manga_up.service.CustomUserDetailsService;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorDao authorDao;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;
    @Mock
    private AuthorMapper authorMapper;

    private static class TestAuthorProjection implements AuthorProjection {
        private final Integer id;
        private final String firstname;
        private final String lastname;
        private final String description;
        private final LocalDate createdAt;
        private final LocalDate birthdate;
        private final String url;
        private final String genre;

        public TestAuthorProjection(Integer id, String firstname, String lastname, String description,
                LocalDate createdAt, LocalDate birthdate, String url, String genre) {
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
            this.description = description;
            this.createdAt = createdAt;
            this.birthdate = birthdate;
            this.url = url;
            this.genre = genre;

        }

        public Integer getId() {
            return id;
        }

        @Override
        public String getFirstname() {
            return firstname;
        }

        @Override
        public String getLastname() {
            return lastname;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public LocalDate getCreatedAt() {
            return createdAt;
        }

        @Override
        public LocalDate getBirthdate() {
            return birthdate;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public String getGenre() {
            return genre;
        }

    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnAllAuthors() throws Exception {
        AuthorProjection author1 = new TestAuthorProjection(
                1,
                "Akira",
                "Toriyama",
                "Mangaka japonais, créateur de Dragon Ball.",
                LocalDate.of(2023, 5, 12),
                LocalDate.of(2023, 5, 12),
                "url",
                "Homme");
        AuthorProjection author2 = new TestAuthorProjection(
                2,
                "Naoko",
                "Takeuchi",
                "Autrice de Sailor Moon.",
                LocalDate.of(2022, 9, 27),
                LocalDate.of(2023, 5, 12),
                "url",
                "Homme");

        Page<AuthorProjection> page = new PageImpl<>(List.of(author1, author2));
        when(authorService.getAllAuthors(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/authors/pagination")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].firstname").value("Akira"))
                .andExpect(jsonPath("$.content[1].lastname").value("Takeuchi"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnAuthorsByid() throws Exception {
        AuthorProjection author = new TestAuthorProjection(
                1,
                "Akira",
                "Toriyama",
                "Mangaka japonais, créateur de Dragon Ball.",
                LocalDate.of(2023, 5, 12),
                LocalDate.of(2023, 5, 12),
                "url",
                "Homme");

        when(authorService.getAuthorById(1)).thenReturn(author);
        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Akira"))
                .andExpect(jsonPath("$.lastname").value("Toriyama"));

    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnCreatedAuthor() throws Exception {
        String json = """
                    {
                        "id": 1,
                        "lastname": "Akira",
                        "firstname": "Toriyama",
                        "genre": "Homme",
                        "description": "Mangaka japonais, créateur de Dragon Ball.",
                        "birthdate": "2023-05-12",
                        "url": "images.com"
                    }
                """;

        AuthorDto a = new AuthorDto(1, "Akira", "Toriyama", "Mangaka japonais, créateur de Dragon Ball.", "Homme",
                LocalDate.of(2023, 5, 12), "images.com");

        when(authorService.save(any())).thenReturn(a);

        mockMvc.perform(post("/api/authors/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value("Toriyama"));
    }

@Test
@WithMockUser(username = "user", roles = { "ADMIN" })
void shouldUpdateCreatedAuthor() throws Exception {
    String json = """
                {
                    "id": 1,
                    "lastname": "Akira",
                    "firstname": "Toriyama",
                    "genre": "Homme",
                    "description": "Mangaka japonais, créateur de Dragon Ball.",
                    "birthdate": "2023-05-12",
                    "url": "images.com"
                }
            """;

    AuthorDto updatedDto = new AuthorDto(1, "Akira", "Toriyama", "Mangaka japonais, créateur de Dragon Ball.",
            "Homme", LocalDate.of(2023, 5, 12), "images.fr");

    
    when(authorService.updateAuthor(eq(1), any(AuthorDto.class))).thenReturn(updatedDto);

    mockMvc.perform(put("/api/authors/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.firstname").value("Toriyama"))
            .andExpect(jsonPath("$.lastname").value("Akira"))
            .andExpect(jsonPath("$.url").value("images.fr"));
}


@Test
@WithMockUser(username = "user", roles = { "ADMIN" })
void shouldDeleteAuthor() throws Exception {
    mockMvc.perform(delete("/api/authors/1").with(csrf()))
           .andExpect(status().isNoContent());

    verify(authorService).deleteAuthorById(1);
}



}
