package manga_up.manga_up.mongo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import manga_up.manga_up.dao.AddressDao;
import manga_up.manga_up.dto.author.AuthorDtoRandom;
import manga_up.manga_up.dto.category.CategoryWithMangaResponse;
import manga_up.manga_up.dto.manga.MangaDtoRandom;
import manga_up.manga_up.mongo.dao.ConnectionLogDao;
import manga_up.manga_up.mongo.model.ConnectionLog;
import manga_up.manga_up.projection.category.CategoryProjection;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.UserAddressService;

@WebMvcTest(ConnectionLogController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ConnectionLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private ConnectionLogDao connectionLogDao;
    @MockitoBean
    private UserAddressService userAddressService;

    @MockitoBean
    private JwtUtils jwtUtils;



    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    public void shouldReturnAllLogs() throws Exception {
        ConnectionLog log1 = new ConnectionLog();
        log1.setId("1");
        log1.setUsername("user1");

        ConnectionLog log2 = new ConnectionLog();
        log2.setId("2");
        log2.setUsername("user2");

        when(connectionLogDao.findAll()).thenReturn(List.of(log1, log2));

        mockMvc.perform(get("/api/logs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }
}
