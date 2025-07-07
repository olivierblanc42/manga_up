package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.author.AuthorLigthDto;
import manga_up.manga_up.model.Author;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(AuthorMapper.class);


  

    public AuthorDto toDtoAuthor(Author author) {
        LOGGER.info("AuthorDto size before mapping: {}", author.getMangas().size());
        return new AuthorDto(
                author.getId(),
                author.getLastname(),
                author.getFirstname(),
                author.getDescription(),
                author.getGenre(),
                author.getBirthdate(),
                author.getUrl()

        );
    }

    public Author toEntity(AuthorDto authorDto) {
        LOGGER.info("Author size before mapping: {}", authorDto.getFirstname());

        Author author = new Author();
        author.setFirstname(Jsoup.clean(authorDto.getFirstname(), Safelist.none()));
        author.setLastname(Jsoup.clean(authorDto.getLastname(), Safelist.none()));
        author.setDescription(Jsoup.clean(authorDto.getDescription(), Safelist.none()));
        author.setGenre(Jsoup.clean(authorDto.getGenre(), Safelist.none()));
        author.setBirthdate(authorDto.getBirthdate());
        author.setUrl(Jsoup.clean(authorDto.getUrl(), Safelist.none()));
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
