package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.author.AuthorLigthDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Author;

@ActiveProfiles("test")

public class AuthorMapperTest {
    private AuthorMapper authorMapper;
       @BeforeEach
    void setUp() {
      
        authorMapper = new AuthorMapper();
  
    } 

@Test
void shouldToDtoAuthor(){
Author author = new Author();
author.setId(1);
author.setLastname("Oda");
author.setFirstname("Eiichiro");
author.setDescription("Auteur de One Piece");
author.setGenre("Homme");
author.setBirthdate(LocalDate.of(1975, 1, 1));
author.setUrl("https://example.com/eiichiro-oda");
author.setMangas(new HashSet<>());



AuthorDto authorDto = authorMapper.toDtoAuthor(author);
 assertNotNull(authorDto);
assertEquals("Oda", authorDto.getLastname());
}

@Test
void shouldToEntity() {
    AuthorDto authorDto = new AuthorDto(
            1,
            "Eiichiro",
            "Oda",
            "Auteur de One Piece",
            "Homme",
            LocalDate.of(1975, 1, 1),
            "https://example.com/eiichiro-oda");
    
    Author author = authorMapper.toEntity(authorDto);
    assertNotNull(author);
    assertEquals("Eiichiro", author.getLastname());
}

@Test
void shouldToLightDtoAuthor() {
    Author author = new Author();
    author.setId(1);

    AuthorLigthDto  authorLigthDto = authorMapper.toLightDtoAuthor(author);
    assertNotNull(authorLigthDto);
    assertEquals(1, authorLigthDto.getId());
}

@Test
void shouldToLightEntity() {
    AuthorLigthDto authorDto = new AuthorLigthDto(
            1
            );

            Author author = authorMapper.toLightEntity(authorDto);
            assertNotNull(author);
    assertEquals(1, author.getId());
}

    @Test
    void shouldToLigthDtoAuthorSet() {
        Author author1 = new Author();
        author1.setId(1);

        Author author2 = new Author();
        author2.setId(2);

        Set<Author> users = Set.of(author1, author2);
        Set<AuthorLigthDto> dtos = authorMapper.toLigthDtoAuthorSet(users);
        assertNotNull(dtos);
        assertEquals(2, dtos.size());

    }

    @Test
    void shouldToEntityAppUserFavoriteSet() {
        AuthorLigthDto authorDto1 = new AuthorLigthDto(
                1);
                AuthorLigthDto authorDto2= new AuthorLigthDto(
                2     );

        Set<AuthorLigthDto> dtos = Set.of(authorDto1, authorDto2);
        Set<Author> users = authorMapper.toEntityAuthors(dtos);
        assertNotNull(users);
        assertEquals(2, users.size());
    }


    @Test
    void shouldToEntityAuthor() {
        AuthorLigthDto authorDto = new AuthorLigthDto(
                1);

        Author author = authorMapper.toEntityAuthor(authorDto);
        assertNotNull(author);
        assertEquals(1, author.getId());
    }

}
