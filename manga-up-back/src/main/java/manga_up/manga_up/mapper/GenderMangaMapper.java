package manga_up.manga_up.mapper;


import manga_up.manga_up.dto.genre.GenreDto;
import manga_up.manga_up.dto.genre.GenreLightDto;
import manga_up.manga_up.model.Genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenderMangaMapper {
    private static final Logger LOGGER= LoggerFactory.getLogger(GenderMangaMapper.class);



    public GenreDto toDtoGenre(Genre genre) {

        return new GenreDto(
                genre.getUrl(),
                genre.getLabel(),
                genre.getDescritpion()   
        );
    }

    public Genre toEntity(GenreDto genreDto) {
        Genre genre = new Genre();
      
        genre.setUrl(genreDto.getUrl());
        genre.setLabel(genreDto.getLabel());
        genre.setDescritpion(genreDto.getDescription());
        return genre;
    }




    public GenreLightDto toGenreLightDto(Genre genre) {
        return new GenreLightDto(
            genre.getId()
            );
    }

    public Genre toEntityGenre(GenreLightDto genreLightDto) {
        Genre genre = new Genre();
        genre.setId(genreLightDto.getId());
        return genre;
    }

    public Set<GenreLightDto> toLightDtoGenres(Set<Genre> genres) {
        return genres.stream()
                .map(this::toGenreLightDto)
                .collect(Collectors.toSet());
    }
    public Set<Genre> toEntityGenres(Set<GenreLightDto> genreLightDtos) {
        return genreLightDtos.stream()
                .map(this::toEntityGenre)
                .collect(Collectors.toSet());
    }


}
