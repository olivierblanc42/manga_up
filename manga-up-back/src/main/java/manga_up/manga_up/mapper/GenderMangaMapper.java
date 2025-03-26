package manga_up.manga_up.mapper;


import manga_up.manga_up.dto.GenreDto;
import manga_up.manga_up.model.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GenderMangaMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(GenderMangaMapper.class);

    public GenderMangaMapper() {

    }

    public GenreDto toDtoGenre(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getLabel(),
                genre.getCreatedAt()
        );
    }



    public Genre toEntity(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setLabel(genreDto.getLabel());
        genre.setCreatedAt(genreDto.getCreatedAt());
        return genre;
    }

}
