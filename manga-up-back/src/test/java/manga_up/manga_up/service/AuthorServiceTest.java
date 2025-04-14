package manga_up.manga_up.service;

import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dto.AuthorDto;
import manga_up.manga_up.mapper.AuthorMapper;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.projection.AuthorProjection;
import manga_up.manga_up.projection.MangaLittleProjection;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthorServiceTest {
 @Mock
    private AuthorDao authorDao;
 @InjectMocks
    private AuthorService authorService;
 @Mock
 private AuthorMapper authorMapper;


 private static class TestAuthorProjection implements AuthorProjection{
private final Integer id;
private final String firstname;
private final String lastname;
private final String description;
private final LocalDate createdAt;
private final Set<MangaLittleProjection> mangas;

public TestAuthorProjection( Integer id, String firstname, String lastname,String description ,LocalDate createdAt, Set<MangaLittleProjection> mangas ) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.description = description;
    this.createdAt = createdAt;
    this.mangas = mangas;
}

     public Integer getId() {
         return id;
     }

     public String getFirstname() {
         return firstname;
     }

     public String getLastname() {
         return lastname;
     }

     public String getDescription() {
         return description;
     }

     public LocalDate getCreatedAt() {
         return createdAt;
     }

     public Set<MangaLittleProjection> getMangas() {
         return mangas;
     }
 }




    @Test
    void shouldReturnAllAuthors() {
     Pageable pageable = PageRequest.of(0, 5);

   AuthorProjection author1 = new TestAuthorProjection(
          1,
          "Akira",
          "Toriyama",
          "Mangaka japonais, créateur de Dragon Ball.",
          LocalDate.of(2023, 5, 12),
          Set.of()
  );
  AuthorProjection author2 = new TestAuthorProjection(
          2,
          "Naoko",
          "Takeuchi",
          "Autrice de Sailor Moon.",
          LocalDate.of(2022, 9, 27),
          Set.of()
  );
     Page<AuthorProjection> page = new PageImpl<>(List.of(author1, author2));
     when(authorDao.findAllByPage(pageable)).thenReturn(page);

     Page<AuthorProjection> result = authorService.getAllAuthors(pageable);

     assertThat(result).hasSize(2).containsExactly(author1, author2);
 }


    @Test
    void shouldReturnAuthorById() {
        AuthorProjection a = new TestAuthorProjection(
                1,
                "Akira",
                "Toriyama",
                "Mangaka japonais, créateur de Dragon Ball.",
                LocalDate.of(2023, 5, 12),
                Set.of()
        );

        when(authorDao.findAuthorProjectionById(1)).thenReturn(Optional.of(a));

        AuthorProjection author = authorService.getAuthorById(1);
        assertThat(author).isEqualTo(a);


    }


    @Test
    void shouldReturnAuthorSave() {
        // Arrange
        AuthorDto authorDto = new AuthorDto("Akira", "Toriyama", "Takuchi");

        Author authorEntity = new Author();
        authorEntity.setFirstname("Akira");
        authorEntity.setLastname("Toriyama");
        authorEntity.setDescription("Takuchi");
        authorEntity.setCreatedAt(LocalDate.now());

        when(authorMapper.toEntity(authorDto)).thenReturn(authorEntity);
        when(authorDao.save(authorEntity)).thenReturn(authorEntity);
        when(authorMapper.toDtoAuthor(authorEntity)).thenReturn(authorDto);

        // Act
        AuthorDto result = authorService.save(authorDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getFirstname()).isEqualTo("Toriyama");
        assertThat(result.getLastname()).isEqualTo("Akira");
        assertThat(result.getDescription()).isEqualTo("Takuchi");
    }



    @Test
    void shouldReturnAuthorUpdate() {
        int id = 1;


        AuthorDto authorDto = new AuthorDto("tite", "Kubo", "bleach");
        Author authorEntity = new Author();
        authorEntity.setId(id);
        authorEntity.setFirstname("Toriyama");
        authorEntity.setLastname("Akira");
        authorEntity.setDescription("Takuchi");
        authorEntity.setCreatedAt(LocalDate.now());

        when(authorDao.findAuthorById(id)).thenReturn(Optional.of(authorEntity));
        authorEntity.setFirstname("tite");
        authorEntity.setLastname("Kubo");
        authorEntity.setDescription("bleach");
        when(authorDao.save(authorEntity)).thenReturn(authorEntity);
        when(authorMapper.toDtoAuthor(authorEntity)).thenReturn(authorDto);

        //Act
        AuthorDto result = authorService.updateAuthor(id, authorDto);
        assertThat(result).isNotNull();
        assertThat(result.getLastname()).isEqualTo("tite");
        assertThat(result.getFirstname()).isEqualTo("Kubo");
        assertThat(result.getDescription()).isEqualTo("bleach");

    }


    @Test
    void shouldDeleteAuthor() {
        Author a = new Author();
        a.setId(1);
        a.setFirstname("Toriyama");
        a.setLastname("Akira");
        a.setDescription("Takuchi");
        a.setCreatedAt(LocalDate.of(2022, 9, 27));
        a.setMangas(Set.of());

        when(authorDao.findAuthorById(1)).thenReturn(Optional.of(a));

        authorService.deleteAuthorById(1);

        verify(authorDao).delete(a);
    }
}