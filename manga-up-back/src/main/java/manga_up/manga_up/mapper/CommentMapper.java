package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.CommentLightDto;
import manga_up.manga_up.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(CommentMapper.class);



    private CommentMapper() {}


    public CommentLightDto toDtoComment(Comment comment) {
        LOGGER.info("Convert comment {}", comment);
        return new CommentLightDto(
                comment.getRating(),
                comment.getComment()
        );
    }

}
