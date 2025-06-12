package manga_up.manga_up.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.model.AppUser;

@Component
public class FavoriteMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserMapper.class);

    // for UserFavoriteDto
    public UserFavoriteDto toDtoAppUserFavorite(AppUser appUser) {
        LOGGER.info("AppUser size before mapping: {}", appUser.getUsername());

        return new UserFavoriteDto(
                appUser.getId(),
                appUser.getUsername());
    }

    public AppUser toEntityAppUserFavorite(UserFavoriteDto userFavoriteDto) {
        LOGGER.info("AppUser size before mapping: {}", userFavoriteDto.getUsername());

        AppUser appUser = new AppUser();
        appUser.setId(userFavoriteDto.getId());
        appUser.setUsername(userFavoriteDto.getUsername());
        return appUser;
    }

    public Set<UserFavoriteDto> toUserFavoriteDtoSet(Set<AppUser> favorite) {
        if (favorite == null) {
            return java.util.Collections.emptySet();
        }
        return favorite.stream()
                .map(this::toDtoAppUserFavorite)
                .collect(Collectors.toSet());
    }

    public Set<AppUser> toEntityAppUserFavoriteSet(Set<UserFavoriteDto> userFavoriteDtos) {
        if (userFavoriteDtos == null) {
            return java.util.Collections.emptySet();
        }
        return userFavoriteDtos.stream()
                .map(this::toEntityAppUserFavorite)
                .collect(Collectors.toSet());
    }
}