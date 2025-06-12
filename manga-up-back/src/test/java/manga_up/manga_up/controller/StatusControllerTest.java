package manga_up.manga_up.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import manga_up.manga_up.dao.StatusDao;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.status.StatusProjection;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.StatusService;

@WebMvcTest(StatusController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatusDao statusDao;
    @MockitoBean
    private StatusService statusService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    private static class TestStatusProjection implements StatusProjection {
        private final Integer id;
        private final String label;

        public TestStatusProjection(Integer id, String label) {
            this.id = id;
            this.label = label;
        }

        public Integer getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnAllStatus() throws Exception {
        TestStatusProjection status1 = new TestStatusProjection(
                1,
                "test");
        TestStatusProjection status2 = new TestStatusProjection(
                1,
                "test");

        Page<StatusProjection> page = new PageImpl<>(List.of(status1, status2));
        when(statusService.findAllByPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/status")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].label").value("test"));
    }
}
