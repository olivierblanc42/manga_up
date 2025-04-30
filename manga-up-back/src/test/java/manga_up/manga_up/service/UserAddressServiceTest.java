package manga_up.manga_up.service;

import manga_up.manga_up.dao.AddressDao;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.mapper.UserAddressMapper;
import manga_up.manga_up.model.UserAddress;
import manga_up.manga_up.projection.appUser.AppUserLittleProjection;
import manga_up.manga_up.projection.userAdress.UserAddressProjection;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserAddressServiceTest {

  @Mock
  private AddressDao addressDao;
  @Mock
  private UserAddressMapper userAddressMapper;
  @InjectMocks
  private UserAddressService userAddressService;

   private static class TestUserAddress implements UserAddressProjection {
       private final  Integer id;
       private final  String line1;
       private final  String line2;
       private final  String line3;
       private final  String city;
       private final LocalDateTime createdAt;
       private final  String postalCode;
       private final Set<AppUserLittleProjection> appUsers;

       private TestUserAddress(Integer id, String line1, String line2, String line3, String city, LocalDateTime createdAt, String postalCode, Set<AppUserLittleProjection> appUsers) {
           this.id = id;
           this.line1 = line1;
           this.line2 = line2;
           this.line3 = line3;
           this.city = city;
           this.createdAt = createdAt;
           this.postalCode = postalCode;
           this.appUsers = appUsers;
       }

       @Override
       public Integer getId() {
           return id;
       }

       @Override
       public String getLine1() {
           return line1;
       }

       @Override
       public String getLine2() {
           return line2;
       }

       @Override
       public String getLine3() {
           return line3;
       }

       @Override
       public String getCity() {
           return city;
       }

       @Override
       public LocalDateTime getCreatedAt() {
           return createdAt;
       }

       @Override
       public String getPostalCode() {
           return postalCode;
       }

       @Override
       public Set<AppUserLittleProjection> getAppUsers() {
           return appUsers;
       }
   }

   private static class TestUserLittleProjection implements AppUserLittleProjection {
       private final Integer id;
       private final String username;
       private final String firstname;
       private final String lastname;

       private TestUserLittleProjection(Integer id, String username, String firstname, String lastname) {
           this.id = id;
           this.username = username;
           this.firstname = firstname;
           this.lastname = lastname;
       }

       @Override
       public Integer getId() {
           return id;
       }

       @Override
       public String getUsername() {
           return username;
       }

       @Override
       public String getFirstname() {
           return firstname;
       }

       @Override
       public String getLastname() {
           return lastname;
       }
   }


    @Test
    void shouldReturnAllUserAddressByPage() {
        Pageable pageable = PageRequest.of(0, 5);


        AppUserLittleProjection projection1 = new TestUserLittleProjection(
                1,
                "Bobo",
                "John",
                "Doe"
        );
        AppUserLittleProjection projection2 = new TestUserLittleProjection(
                2,
                "Bobo",
                "John",
                "Doe"
        );
        Set<AppUserLittleProjection> appUsers = Set.of(projection1, projection2);

        UserAddressProjection userAddress= new TestUserAddress(
                1,
                "123 Main St",
                "Apt 4B",
                "Near the Park",
                "Paris",
                LocalDateTime.now(),
                "75001",
                appUsers
        );
        UserAddressProjection userAddress2 = new TestUserAddress(
                1,
                "1234 Main St",
                "Apt 43B",
                "Near the Park",
                "Paris",
                LocalDateTime.now(),
                "75001",
                appUsers
        );

  Page<UserAddressProjection> page = new PageImpl<>(List.of(userAddress,userAddress2));

   when(addressDao.findAllByPage(pageable)).thenReturn(page);

   Page<UserAddressProjection> result = userAddressService.findAllByPage(pageable);
   assertThat(result).hasSize(2).containsExactly(userAddress, userAddress2);

    }

    @Test
    void findById()  {
        AppUserLittleProjection projection1 = new TestUserLittleProjection(
                1,
                "Bobo",
                "John",
                "Doe"
        );
        Set<AppUserLittleProjection> appUsers = Set.of(projection1);

        UserAddressProjection userAddress= new TestUserAddress(
                1,
                "123 Main St",
                "Apt 4B",
                "Near the Park",
                "Paris",
                LocalDateTime.now(),
                "75001",
                appUsers
        );

        when(addressDao.findUserAddressProjectionById(1)).thenReturn(Optional.of(userAddress));
        UserAddressProjection result = userAddressService.findById(1);
        assertThat(result).isEqualTo(userAddress);


    }

    @Test
    void shouldSaveUserAddress() {
        int id = 1;

        UserAddressDto userAddressDto = new UserAddressDto(
                "123 Main St",
                "Apt 4B",
                "Near the Park",
                "city",
                Instant.now(),
                "75001");

        UserAddress userAddress = new UserAddress();
        userAddress.setId(id);
        userAddress.setLine1(userAddressDto.getLine1());
        userAddress.setLine2(userAddressDto.getLine2());
        userAddress.setLine3(userAddressDto.getLine3());
        userAddress.setCity(userAddressDto.getCity());
        userAddress.setCreatedAt(userAddressDto.getCreatedAt());
        userAddress.setPostalCode(userAddressDto.getPostalCode());

        when(userAddressMapper.toEntity(userAddressDto)).thenReturn(userAddress);
        when(addressDao.save(userAddress)).thenReturn(userAddress);
        when(userAddressMapper.toDto(userAddress)).thenReturn(userAddressDto);

        UserAddressDto result = userAddressService.save(userAddressDto);


        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("city");

    }

    @Test
    void deleteUserAddress() {
       int id = 1;
       UserAddress userAddress = new UserAddress();
       userAddress.setId(id);
       userAddress.setLine1("123 Main St");
       userAddress.setLine2("Apt 4B");
       userAddress.setLine3("Near the Park");
       userAddress.setCity("city");
       userAddress.setCreatedAt(Instant.now());
       userAddress.setPostalCode("75001");

       when(addressDao.findUserAddressById(id)).thenReturn(Optional.of(userAddress));
       userAddressService.deleteUserAddress(1);
       verify(addressDao).delete(userAddress);
    }

    @Test
    void updateUserAddress() {
       int id = 1;
        UserAddressDto userAddressDto = new UserAddressDto(
                "1234 Main St",
                "Apt 4B",
                "Near the Park",
                "city",
                Instant.now(),
                "75001");

        UserAddress userAddressEntity = new UserAddress();
        userAddressEntity.setId(id);
        userAddressEntity.setLine1("1234 Main St");
        userAddressEntity.setLine2("Apt 3B");
        userAddressEntity.setLine3("the Park");
        userAddressEntity.setCity("city2");
        userAddressEntity.setCreatedAt(Instant.now());
        userAddressEntity.setPostalCode("75004");

        when(addressDao.findUserAddressById(id)).thenReturn(Optional.of(userAddressEntity));
        userAddressEntity.setLine1(userAddressDto.getLine1());
        userAddressEntity.setLine2(userAddressDto.getLine2());
        userAddressEntity.setLine3(userAddressDto.getLine3());
        userAddressEntity.setCity(userAddressDto.getCity());
        userAddressEntity.setPostalCode(userAddressDto.getPostalCode());
        when(userAddressMapper.toEntity(userAddressDto)).thenReturn(userAddressEntity);
        when(addressDao.save(userAddressEntity)).thenReturn(userAddressEntity);
        when(userAddressMapper.toDto(userAddressEntity)).thenReturn(userAddressDto);

        UserAddressDto result = userAddressService.updateUserAddress(id, userAddressDto);
        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("city");
        assertThat(result.getLine1()).isEqualTo("1234 Main St");
        assertThat(result.getLine2()).isEqualTo("Apt 4B");
    }
}