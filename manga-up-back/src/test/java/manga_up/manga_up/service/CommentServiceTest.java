package manga_up.manga_up.service;

import manga_up.manga_up.dao.CommentDao;
import manga_up.manga_up.mapper.CommentMapper;
import manga_up.manga_up.model.Comment;
import manga_up.manga_up.projection.AppUserLittleProjection;
import manga_up.manga_up.projection.CommentProjection;
import manga_up.manga_up.projection.MangaLittleProjection;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
@Mock
    private CommentDao commentDao;
@Mock
    private CommentMapper commentMapper;
@InjectMocks
    private CommentService commentService;



    @Test
    void shouldReturnComment() {
        Pageable pageable = PageRequest.of(0, 5);



    }

}