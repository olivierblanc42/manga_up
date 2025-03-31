package manga_up.manga_up.service;


import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.projection.MangaProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MangaService {
    private static final Logger LOGGER= LoggerFactory.getLogger(MangaService.class);

    private final MangaDao mangaDao;

    public MangaService(MangaDao mangaDao) {
        this.mangaDao = mangaDao;
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

}
