package manga_up.manga_up.service;


import manga_up.manga_up.dao.CommentDao;
import manga_up.manga_up.projection.CommentProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


}
