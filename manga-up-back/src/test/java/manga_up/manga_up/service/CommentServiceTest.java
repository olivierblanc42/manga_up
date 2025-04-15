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

   private final class TestCommentProjection implements CommentProjection{
       private final    Integer id;
       private final    Integer rating;
       private final   String comment;
       private final   AppUserLittleProjection idUsers;
       private final   MangaLittleProjection idMangas;

       private TestCommentProjection(Integer id, Integer rating, String comment, AppUserLittleProjection idUsers, MangaLittleProjection idMangas) {
           this.id = id;
           this.rating = rating;
           this.comment = comment;
           this.idUsers = idUsers;
           this.idMangas = idMangas;
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
           return idUsers;
       }

       @Override
       public MangaLittleProjection getIdMangas() {
           return idMangas;
       }
   }

    @Test
    void shouldReturnComment() {
        Pageable pageable = PageRequest.of(0, 5);


    }

}