package manga_up.manga_up.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
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

import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.CommentDao;
import manga_up.manga_up.dto.comment.CommentLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.model.Comment;
import manga_up.manga_up.model.Manga;
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

    @Test
    void getComment_shouldReturnProjectionWhenFound() {
        int id = 42;
        CommentProjection mockProj = mock(CommentProjection.class);

        when(commentDao.findCommentProjectionById(id)).thenReturn(Optional.of(mockProj));

        CommentProjection result = commentService.getComment(id);

        assertNotNull(result);
        assertEquals(mockProj, result);

        verify(commentDao).findCommentProjectionById(id);
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    void getComment_shouldThrowWhenNotFound() {
        int id = 99;
        when(commentDao.findCommentProjectionById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> commentService.getComment(id));
        assertEquals("Comment with id " + id + " not found", ex.getMessage());

        verify(commentDao).findCommentProjectionById(id);
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    void updateComment_shouldReturnDtoWhenFound() {
        // Arrange
        int id = 10;
        CommentLightDto dto = new CommentLightDto(5, "Nouveau commentaire");
        Comment existing = new Comment();
        existing.setId(id);
        existing.setComment("Ancien");
        existing.setRating(3);

        when(commentDao.findCommentById(id)).thenReturn(Optional.of(existing));

        // Act
        CommentLightDto result = commentService.updateComment(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Nouveau commentaire", existing.getComment());
        assertEquals(5, existing.getRating());
        assertSame(dto, result);

        verify(commentDao).findCommentById(id);
        verify(commentDao).save(existing);
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    void updateComment_shouldThrowWhenNotFound() {
        // Arrange
        int id = 99;
        CommentLightDto dto = new CommentLightDto(5, "Nouveau commentaire");

        when(commentDao.findCommentById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> commentService.updateComment(id, dto));

        assertEquals("Comment with id " + id + " not found", ex.getMessage());

        verify(commentDao).findCommentById(id);
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    void deleteComment_shouldRemoveAssociationsAndDeleteWhenFound() {
        // Arrange
        int id = 7;
        Comment comment = new Comment();
        comment.setId(id);
        Manga manga = new Manga();
        AppUser user = new AppUser();
        comment.setIdMangas(manga);
        manga.setComments(new HashSet<>(Set.of(comment)));
        comment.setIdUsers(user);
        user.setComments(new HashSet<>(Set.of(comment)));

        when(commentDao.findCommentById(id)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(id);

        // Assert
        assertFalse(manga.getComments().contains(comment));
        assertNull(comment.getIdMangas());
        assertFalse(user.getComments().contains(comment));
        assertNull(comment.getIdUsers());

        verify(commentDao).findCommentById(id);
        verify(commentDao).delete(comment);
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    void deleteComment_shouldThrowWhenNotFound() {
        // Arrange
        int id = 88;
        when(commentDao.findCommentById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> commentService.deleteComment(id));
        assertEquals("Comment with id " + id + " not found", ex.getMessage());

        verify(commentDao).findCommentById(id);
        verifyNoMoreInteractions(commentDao);
    }

}
