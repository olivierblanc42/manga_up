package manga_up.manga_up.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.GenderUserDao;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.mapper.GenderUserMapper;
import manga_up.manga_up.projection.appUser.AppUserLittleProjection;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.genderUser.GenderUserProjection;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.GenreUserService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GenderUserController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GenderUserTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenderUserDao genderUserDao;

    @MockitoBean
    private GenreUserService genreUserService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Mock
    private GenderUserMapper genderUserMapper;

    private static class AppUserLittleProjectionTest implements AppUserLittleProjection {
        private final Integer id;
        private final String username;
        private final String firstname;
        private final String lastname;

        public AppUserLittleProjectionTest(Integer id, String username, String firstname, String lastname) {
            this.id = id;
            this.username = username;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getFirstname() {
            return firstname;
        }

        @Override
        public String getLastname() {
            return lastname;
        }
    }

    private static class GenderUserProjectionTest implements GenderUserProjection {
        private final Integer id;
        private final String label;
        private final Set<AppUserLittleProjection> appUsers;

        public GenderUserProjectionTest(Integer id, String label, Set<AppUserLittleProjection> appUsers) {
            this.id = id;
            this.label = label;
            this.appUsers = appUsers;
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
        public Set<AppUserLittleProjection> getAppUsers() {
            return appUsers;
        }
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnAllGenderUser() throws Exception {
        AppUserLittleProjectionTest user1 = new AppUserLittleProjectionTest(1, "user1", "John", "Doe");
        AppUserLittleProjectionTest user2 = new AppUserLittleProjectionTest(2, "user2", "Jane", "Smith");

        GenderUserProjectionTest genderTest = new GenderUserProjectionTest(
                100,
                "Non-binaire",
                Set.of(user1));
        GenderUserProjectionTest genderTest2 = new GenderUserProjectionTest(
                110,
                "Homme",
                Set.of(user2));

        Page<GenderUserProjection> page = new PageImpl<>(List.of(genderTest, genderTest2));
        when(genreUserService.getGenreUsers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/gendersUser")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].label").value("Non-binaire"))
                .andExpect(jsonPath("$.content[1].label").value("Homme"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnAllGenderID() throws Exception {
        AppUserLittleProjectionTest user1 = new AppUserLittleProjectionTest(1, "user1", "John", "Doe");

        GenderUserProjectionTest genderTest = new GenderUserProjectionTest(
                100,
                "Non-binaire",
                Set.of(user1));

        when(genreUserService.getGenreUserById(100)).thenReturn(genderTest);

        mockMvc.perform(get("/api/gendersUser/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Non-binaire"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnGenderUser() throws Exception {
        String json = """
                    {
                        "id": 1,
                        "label": "Femmme"
                    }
                """;

        GenderUserDto a = new GenderUserDto(1, "Femmme");

        when(genreUserService.saveGenreUser(any())).thenReturn(a);

        mockMvc.perform(post("/api/gendersUser/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label").value("Femmme"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnGenderUserUpdate() throws Exception {
        String json = """
                    {
                        "id": 1,
                        "label": "Femmme"
                    }
                """;

        GenderUserDto updateDto = new GenderUserDto(1, "Homme");

        when(genreUserService.updateGenreUser(eq(1), any(GenderUserDto.class))).thenReturn(updateDto);

        mockMvc.perform(put("/api/gendersUser/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label").value("Homme"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnInternalServerErrorWhenUpdateFails() throws Exception {
        String json = """
                    {
                        "id": 1,
                        "label": "Femmme"
                    }
                """;

        when(genreUserService.updateGenreUser(eq(1), any(GenderUserDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/api/gendersUser/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldDeleteGenderUser() throws Exception {

        mockMvc.perform(delete("/api/gendersUser/1").with(csrf()))
                .andExpect(status().isOk());

        verify(genreUserService).deleteGenreUserById(1);
    }

}
