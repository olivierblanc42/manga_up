package manga_up.manga_up.mapper;

import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dto.AuthorDto;
import manga_up.manga_up.model.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(AuthorMapper.class);

    private final AuthorDao authorDao;

    public AuthorMapper(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    public AuthorDto toDtoAuthor(Author author) {
        LOGGER.info("Mangas size before mapping: {}", author.getMangas().size());
        return new AuthorDto(
                author.getFirstname(),
                author.getLastname(),
                author.getDescription()

        );
    }

    public Author toEntity(AuthorDto authorDto) {
        Author author = new Author();

        author.setFirstname(authorDto.getFirstname());
        author.setLastname(authorDto.getLastname());
        author.setDescription(authorDto.getDescription());
        return author;
    }


}
