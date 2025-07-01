package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.appUser.UserFavoriteDto;
import manga_up.manga_up.dto.picture.PictureDto;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.model.AppUser;
import manga_up.manga_up.model.Picture;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PictureMapper {

    public PictureDto toPictureDto(Picture picture) {
        return new PictureDto(
                picture.getUrl());
    }

    public Picture toEntity(PictureDto pictureDto) {
        Picture picture = new Picture();
        picture.setUrl(pictureDto.getUrl());
        return picture;
    }

    public PictureLightDto toPictureLightDto(Picture picture) {
        return new PictureLightDto(
                picture.getId(),
                picture.getUrl(),
                picture.getMain());
    }

    public Picture toEntityPicture(PictureLightDto pictureLightDto) {
        Picture picture = new Picture();
        picture.setId(pictureLightDto.getId());
        picture.setUrl(pictureLightDto.getUrl());
        picture.setMain(pictureLightDto.getIsMain());
        return picture;
    }

    public List<PictureLightDto> toPictureLightDtoSet(List<Picture> pictures) {
        return pictures.stream()
                .map(this::toPictureLightDto)
                .collect(Collectors.toList());
    }

    public List<Picture> toEntityPictures(List<PictureLightDto> pictureLightDtos) {
        return pictureLightDtos.stream()
                .map(this::toEntityPicture)
                .collect(Collectors.toList());
    }

    // Set<AppUser> result = new HashSet<>();
    // for (UserFavoriteDto dto : userFavoriteDtos) {
    // AppUser user = toEntityAppUserFavorite(dto);
    // result.add(user);
    // }
    // return result;

}
