package manga_up.manga_up.service;

import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.mapper.AuthorMapper;
import manga_up.manga_up.model.Author;
import manga_up.manga_up.projection.author.AuthorProjection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    private final  LocalDate birthdate;
    private final String url;
    private final String genre;
   

public TestAuthorProjection( Integer id, String firstname, String lastname,String description ,LocalDate createdAt,LocalDate birthdate,String url,String genre ) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.description = description;
    this.createdAt = createdAt;
    this.birthdate =birthdate;
    this.url = url;
    this.genre =genre;
 
}

     public Integer getId() {
         return id;
     }
     @Override
     public String getFirstname() {
         return firstname;
     }
     @Override
     public String getLastname() {
         return lastname;
     }
     @Override
     public String getDescription() {
         return description;
     }
     @Override
     public LocalDate getCreatedAt() {
         return createdAt;
     }
          @Override
     public LocalDate getBirthdate() {
         return birthdate;
     }

          @Override
     public String getUrl() {
         return url;
     }
              @Override
     public String getGenre() {
         return genre;
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
           LocalDate.of(2023, 5, 12),
           "url",
           "Homme"
  );
  AuthorProjection author2 = new TestAuthorProjection(
          2,
          "Naoko",
          "Takeuchi",
          "Autrice de Sailor Moon.",
          LocalDate.of(2022, 9, 27),
                   LocalDate.of(2023, 5, 12),
           "url",
           "Homme"
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
                LocalDate.of(2023, 5, 12),
            "url",
            "Homme"
        );

        when(authorDao.findAuthorProjectionById(1)).thenReturn(Optional.of(a));

        AuthorProjection author = authorService.getAuthorById(1);
        assertThat(author).isEqualTo(a);


    }


@Test
void shouldReturnAuthorSave() {
    // Arrange
    AuthorDto authorDto = new AuthorDto(1,"Akira1", "Toriyama", "description", "Homme", LocalDate.of(2025, 5, 26), "images.com");

    Author authorEntity = new Author();
    authorEntity.setFirstname(authorDto.getFirstname());
    authorEntity.setLastname(authorDto.getLastname());
    authorEntity.setDescription(authorDto.getDescription());
    authorEntity.setGenre(authorDto.getGenre());
    authorEntity.setBirthdate(authorDto.getBirthdate());
    authorEntity.setUrl(authorDto.getUrl());
    authorEntity.setCreatedAt(LocalDate.now());

    when(authorMapper.toEntity(authorDto)).thenReturn(authorEntity);
    when(authorDao.save(authorEntity)).thenReturn(authorEntity);
    when(authorMapper.toDtoAuthor(authorEntity)).thenReturn(authorDto);

    // Act
    AuthorDto result = authorService.save(authorDto);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getLastname()).isEqualTo("Akira1");
    assertThat(result.getFirstname()).isEqualTo("Toriyama");
    assertThat(result.getDescription()).isEqualTo("description");
    assertThat(authorEntity.getCreatedAt()).isNotNull();

    // Verify interactions with mocks
    verify(authorMapper, times(1)).toEntity(authorDto);
    verify(authorDao, times(1)).save(authorEntity);
    verify(authorMapper, times(1)).toDtoAuthor(authorEntity);
}




@Test
void shouldReturnAuthorUpdate() {
    int id = 1;

    // Correction ordre firstname / lastname ici
    AuthorDto authorDto = new AuthorDto(1, "Kubo", "Tite", "bleach", "Homme", LocalDate.of(2025, 5, 26), "images.com");

    Author authorEntity = new Author();
    authorEntity.setId(id);
    authorEntity.setFirstname("Toriyama");
    authorEntity.setLastname("Akira");
    authorEntity.setDescription("Takuchi");
    authorEntity.setCreatedAt(LocalDate.now());

    when(authorDao.findAuthorById(id)).thenReturn(Optional.of(authorEntity));

    when(authorDao.save(authorEntity)).thenReturn(authorEntity);

    when(authorMapper.toDtoAuthor(authorEntity)).thenReturn(authorDto);

    AuthorDto result = authorService.updateAuthor(id, authorDto);

    assertThat(result).isNotNull();
    assertThat(result.getFirstname()).isEqualTo("Tite");
    assertThat(result.getLastname()).isEqualTo("Kubo");
    assertThat(result.getDescription()).isEqualTo("bleach");

    assertThat(authorEntity.getFirstname()).isEqualTo("Tite");
    assertThat(authorEntity.getLastname()).isEqualTo("Kubo");
    assertThat(authorEntity.getDescription()).isEqualTo("bleach");

    verify(authorDao).findAuthorById(id);
    verify(authorDao).save(authorEntity);
    verify(authorMapper).toDtoAuthor(authorEntity);
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

    verify(authorDao).findAuthorById(1);
    verify(authorDao).delete(a);
    verifyNoMoreInteractions(authorDao);
}

@Test
void shouldThrowExceptionWhenDeletingNonExistingAuthor() {
    when(authorDao.findAuthorById(2)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> authorService.deleteAuthorById(2));

    verify(authorDao).findAuthorById(2);
    verifyNoMoreInteractions(authorDao);
}

@Test
void shouldRollbackTransactionOnError() {
    AuthorDto authorDto = new AuthorDto(1,"tite", "Kubo", "bleach", "Homme", LocalDate.of(2025, 5, 26),
            "images.com");
    when(authorMapper.toEntity(authorDto)).thenThrow(new RuntimeException("Simulated error"));

    assertThrows(RuntimeException.class, () -> {
        authorService.save(authorDto);
    });

    // Ici, avec Mockito, on ne peut pas compter en base, donc on vérifie que save n’a jamais été appelé
    verify(authorDao, never()).save(any());
}






    

}