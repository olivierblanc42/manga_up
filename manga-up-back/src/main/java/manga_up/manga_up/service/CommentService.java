package manga_up.manga_up.service;


import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.CommentDao;
import manga_up.manga_up.dto.CommentLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Comment;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.CommentProjection;
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
     * Retrieve all comments
     *
     * @param pageable an object {@link Pageable} which contains all the pagination information
     * @return a results page {@link Page<CommentProjection>} containing comments
     */
   public Page<CommentProjection> getAllComment(Pageable pageable) {
       return commentDao.findAllByPage(pageable);
   }

   public CommentProjection getComment(Integer id) {
       LOGGER.info("Retrieving comment with id {}", id);
       return commentDao.findCommentProjectionById(id)
               .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));
   }


  @Transactional
   public CommentLightDto updateComment(Integer id ,CommentLightDto commentDto) {
       LOGGER.info("Update comment with id {}", id);
       Comment comment = commentDao.findCommentById(id)
               .orElseThrow(() -> new EntityNotFoundException("Address with id " + id + " not found"));
       comment.setComment(commentDto.getComment());
       comment.setRating(commentDto.getRating());
       commentDao.save(comment);
       return commentDto;
   }

  @Transactional
   public void deleteComment(Integer id) {
       LOGGER.info("Deleting comment with id {}", id);
       Comment comment = commentDao.findCommentById(id)
               .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));
       if(!commentDao.existsById(comment.getId())) {
           throw new EntityNotFoundException("Comment with id " + id + " not found");
       }

       Manga manga = comment.getIdMangas();
       if(manga != null) {
           manga.getComments().remove(comment);
           comment.setIdMangas(manga);
       }

       AppUser appUser = comment.getIdUsers();
       if(appUser != null) {
           appUser.getComments().remove(comment);
           comment.setIdUsers(appUser);
       }

       commentDao.delete(comment);
   }


}
