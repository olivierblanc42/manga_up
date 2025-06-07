package manga_up.manga_up.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.status.StatusDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Status;

@ActiveProfiles("test")
public class FavoriteMapperTest {
    private FavoriteMapper favoriteMapper;

    @BeforeEach
    void setUp() {
        favoriteMapper = new FavoriteMapper();
    }

    @Test
    void shouldtoEntityAppUserFavorite() {
        AppUser appUser = new AppUser();
        appUser.setId(1);
        appUser.setUsername("TestUser");

        UserFavoriteDto favoriteDto = favoriteMapper.toDtoAppUserFavorite(appUser);

        assertNotNull(favoriteDto);
        assertEquals(1, favoriteDto.getId());
        assertEquals("TestUser", favoriteDto.getUsername());

    }

    @Test
    void shouldToModel() {

        UserFavoriteDto favoriteDto = new UserFavoriteDto(1, "TestUser");

        AppUser appUser = favoriteMapper.toEntityAppUserFavorite(favoriteDto);

        assertNotNull(appUser);
        assertEquals(1, appUser.getId());
        assertEquals("TestUser", appUser.getUsername());

    }

    @Test
    void shoulToUserFavoriteDtoSet() {
        AppUser user1 = new AppUser();
        user1.setId(1);
        user1.setUsername("Test1");

        AppUser user2 = new AppUser();
        user2.setId(2);
        user2.setUsername("Test2");

        Set<AppUser> users = Set.of(user1, user2);
        Set<UserFavoriteDto> dtos = favoriteMapper.toUserFavoriteDtoSet(users);
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        boolean found = false;
        for (UserFavoriteDto dto : dtos) {
            if ("Test1".equals(dto.getUsername())) {
                found = true;
                break;
            }
        }
        assertTrue(found);

    }

    @Test
    void shouldToEntityAppUserFavoriteSet() {
        UserFavoriteDto user1 = new UserFavoriteDto(2, "Test1");
        UserFavoriteDto user2 = new UserFavoriteDto(2, "Test2");

        Set<UserFavoriteDto> dtosUser = Set.of(user1, user2);
        Set<AppUser> users = favoriteMapper.toEntityAppUserFavoriteSet(dtosUser);
        assertNotNull(users);
        assertEquals(2, users.size());
        boolean found = false;
        for (AppUser user : users) {
            if ("Test1".equals(user.getUsername())) {
                found = true;
                break;
            }
        }
        assertTrue(found);

    }

    @Test
    void shouldToEntityAppUserFavoriteSetFalse() {

        Set<AppUser> users = favoriteMapper.toEntityAppUserFavoriteSet(null);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void shoulToUserFavoriteDtoSetFalse() {

        Set<UserFavoriteDto> dtos = favoriteMapper.toUserFavoriteDtoSet(null);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}
