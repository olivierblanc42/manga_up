package manga_up.manga_up.mapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.comment.CommentDto;
import manga_up.manga_up.dto.comment.CommentLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Comment;
import manga_up.manga_up.model.Manga;

@ActiveProfiles("test")
public class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        commentMapper = new CommentMapper();
    }

    @Test
    void toDtoComment() {

        Manga manga = new Manga();
        manga.setId(1);
        AppUser user = new AppUser();
        user.setId(1);
        Comment comment = new Comment();
        comment.setId(1);
        comment.setComment("comment");
        comment.setRating(4);
        comment.setIdMangas(manga);
        comment.setIdUsers(user);
        comment.setCreatedAt(Instant.now());

        CommentLightDto commentDto = commentMapper.toDtoComment(comment);

    }

}
