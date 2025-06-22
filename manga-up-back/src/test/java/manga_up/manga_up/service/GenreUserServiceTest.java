package manga_up.manga_up.service;

import manga_up.manga_up.dao.GenderUserDao;
import manga_up.manga_up.dto.author.AuthorDto;
import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.mapper.GenderUserMapper;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.GenderUser;
import manga_up.manga_up.projection.appUser.AppUserLittleProjection;
import manga_up.manga_up.projection.genderUser.GenderUserProjection;
import manga_up.manga_up.projection.genre.GenreProjection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GenreUserServiceTest {
    @Mock
    private GenderUserDao genderUserDao;
    @Mock
    private GenderUserMapper genderUserMapper;
    @InjectMocks
    private GenreUserService genreUserService;

    private static class TestGenderUserProjection implements GenderUserProjection {
        private final Integer id;
        private final String label;
        private final Set<AppUserLittleProjection> appUsers;

        private TestGenderUserProjection(Integer id, String label, Set<AppUserLittleProjection> appUsers) {
            this.id = id;
            this.label = label;
            this.appUsers = appUsers;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public Set<AppUserLittleProjection> getAppUsers() {
            return appUsers;
        }
    }

    @Test
    void shouldReturnAllGenreUsersUsingMockedProjections() {
        Pageable pageable = PageRequest.of(0, 5);

        GenderUserProjection gender1 = mock(GenderUserProjection.class);
        GenderUserProjection gender2 = mock(GenderUserProjection.class);

        Page<GenderUserProjection> page = new PageImpl<>(List.of(gender1, gender2));
        when(genderUserDao.getGenderUser(pageable)).thenReturn(page);

        Page<GenderUserProjection> result = genreUserService.getGenreUsers(pageable);

        assertThat(result).hasSize(2).containsExactly(gender1, gender2);
    }

    @Test
    void shouldReturnGenreUserByIdUsingMockedProjections() {

        GenderUserProjection gender = mock(GenderUserProjection.class);

        when(genderUserDao.findGenderUserProjectionById(1)).thenReturn(Optional.of(gender));

        GenderUserProjection result = genreUserService.getGenreUserById(1);
        assertThat(result).isEqualTo(gender);

    }



    @Test
    void shouldReturnAllGenreUsers() {
        Pageable pageable = PageRequest.of(0, 5);

        GenderUserProjection gender1 = new TestGenderUserProjection(
                1,
                "Man",
                Set.of());
        GenderUserProjection gender2 = new TestGenderUserProjection(
                1,
                "Woman",
                Set.of());

        Page<GenderUserProjection> page = new PageImpl<>(List.of(gender1, gender2));
        when(genderUserDao.getGenderUser(pageable)).thenReturn(page);

        Page<GenderUserProjection> result = genreUserService.getGenreUsers(pageable);

        assertThat(result).hasSize(2).containsExactly(gender1, gender2);
    }

    @Test
    void shouldReturnGenreUserById() {

        GenderUserProjection gender = new TestGenderUserProjection(
                1,
                "Man",
                Set.of());

        when(genderUserDao.findGenderUserProjectionById(1)).thenReturn(Optional.of(gender));

        GenderUserProjection result = genreUserService.getGenreUserById(1);
        assertThat(result).isEqualTo(gender);

    }






    @Test
    void shouldThrowExceptionWhenGenderUserHasUser() {
        GenderUser gender = new GenderUser();
        gender.setId(1);
        gender.setLabel("Man");
        gender.setAppUsers(Set.of());
        AppUser user = new AppUser();
        gender.setAppUsers(Set.of(user));
        when(genderUserDao.findGenderById(1)).thenReturn(Optional.of(gender));


  EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
      genreUserService.deleteGenreUserById(1);
  });

           assertEquals("The Gender user is linked to a user and cannot be deleted", exception.getMessage());
           verify(genderUserDao, never()).delete(any());

    }



    @Test
    void deleteGenreUserById() {
        GenderUser gender = new GenderUser();
        gender.setId(1);
        gender.setLabel("Man");
        gender.setAppUsers(Set.of());
        when(genderUserDao.findGenderById(1)).thenReturn(Optional.of(gender));
        genreUserService.deleteGenreUserById(1);
        verify(genderUserDao).delete(gender);

    }






    @Test
    void saveGenreUser() {
        GenderUserDto genderUserDto = new GenderUserDto(1, "Man");
        GenderUser genderUserEntity = new GenderUser();
        genderUserEntity.setId(genderUserDto.getId());
        genderUserEntity.setLabel(genderUserDto.getLabel());

        when(genderUserMapper.toEntity(genderUserDto)).thenReturn(genderUserEntity);
        when(genderUserDao.save(genderUserEntity)).thenReturn(genderUserEntity);
        when(genderUserMapper.toDto(genderUserEntity)).thenReturn(genderUserDto);

        GenderUserDto result = genreUserService.saveGenreUser(genderUserDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(genderUserDto);
    }    


@Test
void shouldThrowExceptionWhenSaveErreur() {
    GenderUserDto genderUserDto = new GenderUserDto(1, "Man");
    GenderUser genderUserEntity = new GenderUser();
    genderUserEntity.setId(genderUserDto.getId());
    genderUserEntity.setLabel(genderUserDto.getLabel());

    when(genderUserMapper.toEntity(genderUserDto)).thenReturn(genderUserEntity);
    when(genderUserDao.save(genderUserEntity)).thenThrow(new RuntimeException("Error saving GenderUser"));

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        genreUserService.saveGenreUser(genderUserDto);
    });

    assertEquals("Error saving GenderUser", exception.getMessage());

    // Verify
    verify(genderUserMapper).toEntity(genderUserDto);
    verify(genderUserDao).save(genderUserEntity);
}






    @Test
    void ShouldUpdateGenreUser() {
        int id = 1;
        GenderUserDto genderDto = new GenderUserDto(1, "Man3");
        GenderUser genderEntity = new GenderUser();
        genderEntity.setId(id);
        genderEntity.setLabel("Man");

        when(genderUserDao.findGenderById(id)).thenReturn(Optional.of(genderEntity));
        when(genderUserDao.save(genderEntity)).thenReturn(genderEntity);
        when(genderUserMapper.toDto(genderEntity)).thenReturn(genderDto); 

        GenderUserDto result = genreUserService.updateGenreUser(id, genderDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(genderDto);
        assertThat(result.getLabel()).isEqualTo("Man3");
    }





    @Test
    void shouldReturnAllGenreUsersDto() {

        GenderUserDto genderDto1 = new GenderUserDto(1, "Man3");
        GenderUserDto genderDto2 = new GenderUserDto(1, "Man3");

        List<GenderUserDto> genderUsersList = new ArrayList<>(List.of(genderDto1, genderDto2));
        when(genderUserDao.getGenderUserDto()).thenReturn(genderUsersList);


        List<GenderUserDto> result = genreUserService.getAllGenreUsers();

        assertThat(result).hasSize(2).containsExactly(genderDto1, genderDto2);

    }
}