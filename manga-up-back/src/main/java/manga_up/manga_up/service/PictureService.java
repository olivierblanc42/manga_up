package manga_up.manga_up.service;

import manga_up.manga_up.dao.PictureDao;
import manga_up.manga_up.dto.PictureDto;
import manga_up.manga_up.mapper.PictureMapper;
import manga_up.manga_up.model.Picture;
import manga_up.manga_up.projection.PictureProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PictureService {
    private static final Logger LOGGER= LoggerFactory.getLogger(PictureService.class);

    private final PictureDao pictureDao;
    private final PictureMapper pictureMapper;

    public PictureService(PictureDao pictureDao, PictureMapper pictureMapper) {
        this.pictureDao=pictureDao;
        this.pictureMapper = pictureMapper;
    }




    /**
     * Récupère une page paginée d'images.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Address >} contenant les images
     */
    public Page<PictureProjection> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all addresses by Pageable");
        return pictureDao.findAllByPage(pageable);
    }





    public PictureDto UpdatePicture(Integer id, PictureDto pictureDto) {
        LOGGER.info("Update picture");
        Picture picture = pictureDao.findPictureById(id).
                orElseThrow(() -> new RuntimeException("Picture not found"));
        picture.setUrl(pictureDto.getUrl());
        pictureDao.save(picture);
        return pictureMapper.toPictureDto(picture);
    }

}
