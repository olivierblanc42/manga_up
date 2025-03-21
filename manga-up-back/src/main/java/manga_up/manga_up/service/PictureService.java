package manga_up.manga_up.service;

import manga_up.manga_up.dao.PictureDao;
import manga_up.manga_up.model.Address;
import manga_up.manga_up.model.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PictureService {
    private static final Logger LOGGER= LoggerFactory.getLogger(PictureService.class);

    private final PictureDao pictureDao;
    public PictureService(PictureDao pictureDao) {
        this.pictureDao=pictureDao;
    }




    /**
     * Récupère une page paginée d'images.
     *
     * @param pageable un objet {@link Pageable} qui contient les informations de pagination et de tri
     * @return une page de résultats {@link Page < Address >} contenant les images
     */
    public Page<Picture> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all addresses by Pageable");
        return pictureDao.findAllByPage(pageable);
    }

}
