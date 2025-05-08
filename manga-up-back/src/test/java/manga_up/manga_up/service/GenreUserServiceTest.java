// package manga_up.manga_up.service;

// import manga_up.manga_up.dao.GenderUserDao;
// import manga_up.manga_up.dto.genderUser.GenderUserDto;
// import manga_up.manga_up.mapper.CategoryMapper;
// import manga_up.manga_up.mapper.GenderUserMapper;
// import manga_up.manga_up.model.Category;
// import manga_up.manga_up.model.GenderUser;
// import manga_up.manga_up.projection.appUser.AppUserLittleProjection;
// import manga_up.manga_up.projection.author.AuthorProjection;
// import manga_up.manga_up.projection.genderUser.GenderUserProjection;
// import manga_up.manga_up.projection.manga.MangaLittleProjection;

// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;

// import java.time.LocalDate;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// @SpringBootTest
// class GenreUserServiceTest {
// @Mock
// private GenderUserDao genderUserDao;
// @Mock
// private GenderUserMapper genderUserMapper;
// @InjectMocks
// private GenreUserService genreUserService;


//     private static class TestGenderUserProjection implements GenderUserProjection {
//         private final  Integer id;
//         private final String label;
//         private final Set<AppUserLittleProjection> appUsers;

//         private TestGenderUserProjection(Integer id, String label, Set<AppUserLittleProjection> appUsers) {
//             this.id = id;
//             this.label = label;
//             this.appUsers = appUsers;
//         }

//         @Override
//         public Integer getId() {
//             return id;
//         }

//         @Override
//         public String getLabel() {
//             return label;
//         }

//         @Override
//         public Set<AppUserLittleProjection> getAppUsers() {
//             return appUsers;
//         }
//     }



//     @Test
//     void shouldReturnAllGenreUsers() {
//         Pageable pageable = PageRequest.of(0, 5);

//         GenderUserProjection gender1 = new TestGenderUserProjection(
//                 1,
//                 "Man",
//                 Set.of()
//         );
//         GenderUserProjection gender2 = new TestGenderUserProjection(
//                 1,
//                 "Woman",
//                 Set.of()
//         );

//         Page<GenderUserProjection> page = new PageImpl<>(List.of(gender1, gender2));
//         when(genderUserDao.getGenderUser(pageable)).thenReturn(page);

//         Page<GenderUserProjection> result = genreUserService.getGenreUsers(pageable);

//         assertThat(result).hasSize(2).containsExactly(gender1, gender2);
//     }

//     @Test
//     void shouldReturnGenreUserById() {

//         GenderUserProjection gender = new TestGenderUserProjection(
//                 1,
//                 "Man",
//                 Set.of()
//         );

//         when(genderUserDao.findGenderUserProjectionById(1)).thenReturn(Optional.of(gender));

//         GenderUserProjection result = genreUserService.getGenreUserById(1);
//         assertThat(result).isEqualTo(gender);

//     }

//     @Test
//     void deleteGenreUserById() {
//         GenderUser gender = new GenderUser();
//         gender.setId(1);
//         gender.setLabel("Man");
//         gender.setAppUsers(Set.of());
//         when(genderUserDao.findGenderById(1)).thenReturn(Optional.of(gender));
//         genreUserService.deleteGenreUserById(1);
//         verify(genderUserDao).delete(gender);

//     }

//     @Test
//     void saveGenreUser() {
//         int id = 1;
//         GenderUserDto genderDto = new GenderUserDto(
//                 "Man"
//         );
//         GenderUser genderEntity = new GenderUser();
//         genderEntity.setId(id);
//         genderEntity.setLabel("Man");

//         when(genderUserMapper.toEntity(genderDto)).thenReturn(genderEntity);
//         when(genderUserDao.save(genderEntity)).thenReturn(genderEntity);
//         when(genderUserMapper.toDto(genderEntity)).thenReturn(genderDto);

//         GenderUserDto result = genreUserService.saveGenreUser(genderDto);

//         assertThat(result).isNotNull();
//         assertThat(result).isEqualTo(genderDto);
//     }

//     @Test
//     void ShouldUpdateGenreUser() {
//         int id = 1;
//         GenderUserDto genderDto = new GenderUserDto(
//                 "Man3"
//         );
//         GenderUser genderEntity = new GenderUser();
//         genderEntity.setId(id);
//         genderEntity.setLabel("Man");

//         when(genderUserDao.findGenderById(1)).thenReturn(Optional.of(genderEntity));
//         genderEntity.setLabel(genderDto.getLabel());
//         when(genderUserDao.save(genderEntity)).thenReturn(genderEntity);
//         when(genderUserMapper.toDto(genderEntity)).thenReturn(genderDto);

//         GenderUserDto result = genreUserService.updateGenreUser(id ,genderDto);
//         assertThat(result).isNotNull();
//         assertThat(result).isEqualTo(genderDto);
//         assertThat(result.getLabel()).isEqualTo("Man3");
//     }
// }