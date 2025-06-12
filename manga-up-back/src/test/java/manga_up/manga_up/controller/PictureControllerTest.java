package manga_up.manga_up.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import manga_up.manga_up.dao.PictureDao;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.picture.PictureDto;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.mapper.AuthorMapper;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.manga.MangaLittleProjection;
import manga_up.manga_up.projection.pictureProjection.PictureProjection;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.PictureService;

@WebMvcTest(PictureController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PictureControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PictureDao pictureDao;
    @MockitoBean
    private PictureService pictureService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;
    @Mock
    private AuthorMapper authorMapper;

    private static class MangaLittleProjectionTest implements MangaLittleProjection {
        private final Integer id;
        private final String title;

        public MangaLittleProjectionTest(Integer id, String title) {
            this.id = id;
            this.title = title;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String getTitle() {
            return title;
        }
    }

    private static class TestPictureProjection implements PictureProjection {
        private final Integer id;
        private final String url;
        private final Boolean isMain;
        private final MangaLittleProjectionTest idMangas;

        public TestPictureProjection(Integer id, String url, Boolean isMain, MangaLittleProjectionTest idMangas) {
            this.id = id;
            this.url = url;
            this.isMain = isMain;
            this.idMangas = idMangas;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public Boolean getIsMain() {
            return isMain;
        }

        @Override
        public MangaLittleProjectionTest getIdMangas() {
            return idMangas;
        }

    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnAllPicture() throws Exception {
        MangaLittleProjectionTest manga1 = new MangaLittleProjectionTest(1, "One Piece");
        MangaLittleProjectionTest manga2 = new MangaLittleProjectionTest(2, "Naruto");

        TestPictureProjection picture1 = new TestPictureProjection(100, "https://example.com/onepiece.jpg", true,
                manga1);
        TestPictureProjection picture2 = new TestPictureProjection(101, "https://example.com/naruto.jpg", false,
                manga2);

        Page<PictureProjection> page = new PageImpl<>(List.of(picture1, picture2));
        when(pictureService.findAllByPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/picture")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].url").value("https://example.com/onepiece.jpg"))
                .andExpect(jsonPath("$.content[1].url").value("https://example.com/naruto.jpg"));
    }


    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnPicture() throws Exception {
        MangaLittleProjectionTest manga1 = new MangaLittleProjectionTest(1, "One Piece");

        TestPictureProjection picture1 = new TestPictureProjection(100, "https://example.com/onepiece.jpg", true,
                manga1);
 

        when(pictureService.findById(1)).thenReturn(picture1);

        mockMvc.perform(get("/api/picture/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://example.com/onepiece.jpg"));
    }




 
@Test
@WithMockUser(username = "user", roles = { "ADMIN" })
void shouldUpdateCreatedPicture() throws Exception {
    String json = """
                {
              "id": 1,
              "url": "string",
              "isMain": true
                }
            """;

    PictureLightDto  updatedDto = new PictureLightDto( 1,
     "string2",
     true
      );

    when(pictureService.updatePicture(eq(1), any(PictureLightDto.class))).thenReturn(updatedDto);

    mockMvc.perform(put("/api/picture/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.url").value("string2"));
}


@Test
@WithMockUser(username = "user", roles = { "ADMIN" })
void shouldReturnInternalServerErrorWhenUpdateFails() throws Exception {
    String json = """
                {
              "id": 1,
              "url": "string",
              "isMain": true
                }
            """;

    // Simuler une exception lancée par le service
    when(pictureService.updatePicture(eq(1), any(
            PictureLightDto.class)))
            .thenThrow(new RuntimeException("Database error"));

    mockMvc.perform(put("/api/picture/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .with(csrf()))
            .andExpect(status().isInternalServerError());
}







    @Test
@WithMockUser(username = "user", roles = { "ADMIN" })
void shouldDeleteAuthor() throws Exception {
    mockMvc.perform(delete("/api/picture/1").with(csrf()))
           .andExpect(status().isOk());

    verify(pictureService).deletePictureById(1);
}

}
