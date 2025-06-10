package manga_up.manga_up.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.CommentDao;
import manga_up.manga_up.projection.appUser.AppUserLittleProjection;
import manga_up.manga_up.projection.comment.CommentProjection;
import manga_up.manga_up.projection.manga.MangaLittleProjection;
import manga_up.manga_up.projection.userAdress.UserAddressProjection;
import manga_up.manga_up.service.CommentService;
import manga_up.manga_up.service.CustomUserDetailsService;

@WebMvcTest(CommentController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentControlleurTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CommentDao commentDao;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @MockitoBean
    CommentService commentService;
    @MockitoBean
    private JwtUtils jwtUtils;

public static class CommentProjectionTest implements CommentProjection {

    private Integer id;
    private Integer rating;
    private String comment;
    private AppUserLittleProjectionTest user;
    private MangaLittleProjectionTest manga;

    public CommentProjectionTest(Integer id, Integer rating, String comment,
                                 AppUserLittleProjectionTest user,
                                 MangaLittleProjectionTest manga) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.manga = manga;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getRating() {
        return rating;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public AppUserLittleProjection getIdUsers() {
        return user;
    }

    @Override
    public MangaLittleProjection getIdMangas() {
        return manga;
    }

}

public static class MangaLittleProjectionTest implements MangaLittleProjection {
    private Integer id;
    private String title;

    public MangaLittleProjectionTest(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }
}

public static class AppUserLittleProjectionTest implements AppUserLittleProjection {
    private Integer id;
    private String username;
    private String firstname;
    private String lastname;

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

@Test
@WithMockUser(username = "user", roles = { "ADMIN" })
void shoudlReturnfindAllByPage() throws Exception {
        AppUserLittleProjectionTest user1 = new AppUserLittleProjectionTest(1, "john_doe", "John", "Doe");
        MangaLittleProjectionTest manga1 = new MangaLittleProjectionTest(101, "Naruto");
        CommentProjectionTest comment1 = new CommentProjectionTest(10, 5, "Excellent manga !", user1, manga1);

        AppUserLittleProjectionTest user2 = new AppUserLittleProjectionTest(2, "jane_smith", "Jane", "Smith");
        MangaLittleProjectionTest manga2 = new MangaLittleProjectionTest(202, "One Piece");
        CommentProjectionTest comment2 = new CommentProjectionTest(11, 4, "Très bon mais un peu long", user2, manga2);




        List<CommentProjection> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        Page<CommentProjection> page = new PageImpl<>(comments);

        when(commentService.getAllComments(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/comments")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].rating").value(5))
                .andExpect(jsonPath("$.content[1].rating").value(4));
}

@Test
@WithMockUser(username = "user", roles = { "ADMIN" })
void shoudlReturnfindAllById() throws Exception {
    AppUserLittleProjectionTest user = new AppUserLittleProjectionTest(1, "john_doe", "John", "Doe");
    MangaLittleProjectionTest manga = new MangaLittleProjectionTest(101, "Naruto");
    CommentProjectionTest comment = new CommentProjectionTest(10, 5, "Excellent manga !", user, manga);



  

    when(commentService.getComment(1)).thenReturn(comment);

    mockMvc.perform(get("/api/comments/1")
       )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rating").value(5));          
}






@Test
@WithMockUser(username = "user", roles = { "ADMIN" })
void shouldDeleteAuthor() throws Exception {
    mockMvc.perform(delete("/api/comments/1").with(csrf()))
            .andExpect(status().isOk());

    verify(commentService).deleteComment(1);

    ;
}

}
