package manga_up.manga_up.mapper;

import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dto.AuthorDto;
import manga_up.manga_up.dto.AuthorLigthDto;
import manga_up.manga_up.model.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(AuthorMapper.class);

    private final AuthorDao authorDao;

    public AuthorMapper(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    public AuthorDto toDtoAuthor(Author author) {
        LOGGER.info("AuthorDto size before mapping: {}", author.getMangas().size());
        return new AuthorDto(
                author.getFirstname(),
                author.getLastname(),
                author.getDescription()

        );
    }

    public Author toEntity(AuthorDto authorDto) {
        LOGGER.info("Author size before mapping: {}", authorDto.getFirstname());

        Author author = new Author();
        author.setFirstname(authorDto.getFirstname());
        author.setLastname(authorDto.getLastname());
        author.setDescription(authorDto.getDescription());
        return author;
    }


    public AuthorLigthDto toLightDtoAuthor(Author author) {
        return new AuthorLigthDto(
                author.getId()
        );
    }

    public Author toLightEntity(AuthorLigthDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        return author;
    };

    public Set<AuthorLigthDto> toLigthDtoAuthorSet(Set<Author> authors) {
        return authors.stream()
                .map(this::toLightDtoAuthor)
                .collect(Collectors.toSet());
    }
    public Set<Author> toEntityAuthors(Set<AuthorLigthDto> authorLightDtos) {
        return authorLightDtos.stream()
                .map(this::toEntityAuthor)
                .collect(Collectors.toSet());
    }

    public Author toEntityAuthor(AuthorLigthDto authorLightDto) {
        Author author = new Author();
        author.setId(authorLightDto.getId());
        return author;
    }

}
