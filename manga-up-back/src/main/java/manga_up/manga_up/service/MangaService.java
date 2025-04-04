package manga_up.manga_up.service;


import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dao.PictureDao;
import manga_up.manga_up.dto.MangaDto;
import manga_up.manga_up.dto.PictureLightDto;
import manga_up.manga_up.mapper.MangaMapper;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.MangaProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MangaService {
    private static final Logger LOGGER= LoggerFactory.getLogger(MangaService.class);

    private final MangaDao mangaDao;
    private final MangaMapper mangaMapper;
    private final PictureDao pictureDao;

    public MangaService(MangaDao mangaDao, MangaMapper mangaMapper, PictureDao pictureDao) {
        this.mangaDao = mangaDao;
        this.mangaMapper = mangaMapper;
        this.pictureDao = pictureDao;
    }

    /**
     * Récupère une page paginée de mangas.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Manga >} contenant les mangas
     */
    public Page<MangaProjection> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all mangas by Pageable");
        return mangaDao.findAllMangas(pageable);
    }



    public MangaDto save(MangaDto mangaDto) {
        LOGGER.info("Save manga");
        LOGGER.info("manga mangaDto : {}", mangaDto);

        if (mangaDto.getPictures() == null || mangaDto.getPictures().isEmpty()) {
            LOGGER.error("Validation failed: A manga must have at least one image.");
            throw new IllegalArgumentException("A manga must contain at least one image.");
        }
        for (PictureLightDto pictureDto : mangaDto.getPictures()) {
            if (!pictureDao.existsById(pictureDto.getId())) {
                LOGGER.error("Validation failed: Image with ID {} does not exist.", pictureDto.getId());
                throw new IllegalArgumentException("Image with ID " + pictureDto.getId() + " does not exist.");
            }
        }

        Manga manga = mangaMapper.mangaToEntity(mangaDto);
        if (manga.getPriceHt() != null) {
            BigDecimal priceHt = manga.getPriceHt();
            BigDecimal multiplier = new BigDecimal("0.1");
            BigDecimal TVAmount = priceHt.multiply(multiplier);
            manga.setPriceHt(priceHt.add(TVAmount));
            LOGGER.info("Price adjusted with TVA (10%): {} → {}", priceHt, manga.getPriceHt());
        }else {
            LOGGER.warn("The price excluding TVA is NULL, no TVA calculation carried out!");
        }

        try{
            mangaDao.save(manga);
        }catch (Exception e){
            LOGGER.error("Error saving manga", e);
            throw new RuntimeException("Error saving manga",e);
        }
        return mangaMapper.mangaToMangaDto(manga);
    }



    /**
     * Retrieve Four mangas
     * return a list of four mangas
     */
    public List<MangaProjection> getRandomFourMangas(Pageable pageable){

        return mangaDao.findRandomMangas(PageRequest.of(0, 4));
    }

    /**
     * Retrieve a Random manga
     * return a list of one manga
     */
    public List<MangaProjection> getRandomManga(Pageable pageable){
        return mangaDao.findRandomMangas(PageRequest.of(0, 1));
    }

}
