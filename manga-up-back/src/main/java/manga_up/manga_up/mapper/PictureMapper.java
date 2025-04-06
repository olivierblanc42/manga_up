package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.PictureDto;
import manga_up.manga_up.dto.PictureLightDto;
import manga_up.manga_up.model.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PictureMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(PictureMapper.class);


    public PictureDto toPictureDto(Picture picture) {
        return new PictureDto(
                picture.getUrl()
        );
    }

    public Picture toEntity(PictureDto pictureDto) {
        Picture picture = new Picture();
        picture.setUrl(pictureDto.getUrl());
        return picture;
    }




   public PictureLightDto toPictureLightDto(Picture picture) {
       return new PictureLightDto(
               picture.getId()
       );
   }

   public Picture toEntityPicture(PictureLightDto pictureLightDto) {
       Picture picture = new Picture();
       picture.setId(pictureLightDto.getId());
       return picture;
   }

   public Set<PictureLightDto> toPictureLightDtoSet(Set<Picture> pictures) {
       return pictures.stream()
               .map(this::toPictureLightDto)
               .collect(Collectors.toSet());
   }

    public Set<Picture> toEntityPictures(Set<PictureLightDto> pictureLightDtos) {
        return pictureLightDtos.stream()
                .map(this::toEntityPicture)
                .collect(Collectors.toSet());
    }
}
