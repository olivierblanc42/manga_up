package manga_up.manga_up.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dao.CommentDao;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.model.Comment;
import manga_up.manga_up.projection.comment.CommentProjection;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CommentServiceTest {

    @Mock
    private CommentDao commentDao;

    @InjectMocks
    private CommentService commentService;

    @Test
    void getAllComments_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 5);

        CommentProjection mockComment = mock(CommentProjection.class);

        List<CommentProjection> comments = List.of(mockComment);

        Page<CommentProjection> page = new PageImpl<>(comments, pageable, comments.size());

        when(commentDao.findAllByPage(pageable)).thenReturn(page);

        Page<CommentProjection> result = commentService.getAllComments(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(commentDao).findAllByPage(pageable);
    }




}
