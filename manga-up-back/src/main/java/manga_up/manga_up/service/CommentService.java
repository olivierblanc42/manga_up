package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.CommentDao;
import manga_up.manga_up.dto.comment.CommentLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Comment;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.comment.CommentProjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    private final CommentDao commentDao;

    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    /**
     * Retrieves a paginated list of comments.
     *
     * @param pageable pagination and sorting information
     * @return a {@link Page} of {@link CommentProjection} containing the comments
     */
    public Page<CommentProjection> getAllComments(Pageable pageable) {
        return commentDao.findAllByPage(pageable);
    }

    /**
     * Retrieves a comment by its id.
     *
     * @param id the id of the comment
     * @return the {@link CommentProjection} found
     * @throws EntityNotFoundException if no comment with the given id is found
     */
    public CommentProjection getComment(Integer id) {
        LOGGER.info("Retrieving comment with id {}", id);
        return commentDao.findCommentProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));
    }

    /**
     * Updates a comment by its id.
     *
     * @param id         the id of the comment to update
     * @param commentDto the DTO containing updated comment data
     * @return the updated {@link CommentLightDto}
     * @throws EntityNotFoundException if the comment to update is not found
     */
    @Transactional
    public CommentLightDto updateComment(Integer id, CommentLightDto commentDto) {
        LOGGER.info("Updating comment with id {}", id);
        Comment comment = commentDao.findCommentById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));
        comment.setComment(commentDto.getComment());
        comment.setRating(commentDto.getRating());
        commentDao.save(comment);
        return commentDto;
    }

    /**
     * Deletes a comment by its id.
     * Also removes the comment from associated Manga and AppUser entities.
     *
     * @param id the id of the comment to delete
     * @throws EntityNotFoundException if the comment is not found
     */
    @Transactional
    public void deleteComment(Integer id) {
        LOGGER.info("Deleting comment with id {}", id);
        Comment comment = commentDao.findCommentById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));

        Manga manga = comment.getIdMangas();
        if (manga != null) {
            manga.getComments().remove(comment);
            comment.setIdMangas(null);
        }

        AppUser appUser = comment.getIdUsers();
        if (appUser != null) {
            appUser.getComments().remove(comment);
            comment.setIdUsers(null);
        }

        commentDao.delete(comment);
    }
}
