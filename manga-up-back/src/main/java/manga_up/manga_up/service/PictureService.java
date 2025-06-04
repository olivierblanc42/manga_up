package manga_up.manga_up.service;

import jakarta.persistence.EntityNotFoundException;
import manga_up.manga_up.dao.MangaDao;
import manga_up.manga_up.dao.PictureDao;
import manga_up.manga_up.dto.picture.PictureLightDto;
import manga_up.manga_up.mapper.PictureMapper;
import manga_up.manga_up.model.Manga;
import manga_up.manga_up.model.Picture;
import manga_up.manga_up.projection.pictureProjection.PictureProjection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PictureService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PictureService.class);

    private final PictureDao pictureDao;
    private final PictureMapper pictureMapper;
    private final MangaDao mangaDao;

    public PictureService(PictureDao pictureDao, PictureMapper pictureMapper, MangaDao mangaDao) {
        this.pictureDao = pictureDao;
        this.pictureMapper = pictureMapper;
        this.mangaDao = mangaDao;
    }

    /**
     * Retrieves a paginated list of pictures.
     *
     * @param pageable a {@link Pageable} object containing pagination and sorting
     *                 information
     * @return a {@link Page} of {@link PictureProjection} containing the pictures
     */
    public Page<PictureProjection> findAllByPage(Pageable pageable) {
        LOGGER.info("Find all addresses by Pageable");
        return pictureDao.findAllByPage(pageable);
    }

    /**
     * Retrieves a picture projection by its ID.
     *
     * @param id the ID of the picture to retrieve
     * @return the {@link PictureProjection} matching the given ID
     * @throws EntityNotFoundException if no picture is found with the given ID
     */
    public PictureProjection findById(Integer id) {
        return pictureDao.findPictureProjectionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gender user with id " + id + " not found"));
    }

    /**
     * Updates a picture with the given ID using the provided data.
     * If the picture is marked as main, all other pictures for the associated manga
     * will have their main flag removed.
     *
     * @param id         the ID of the picture to update
     * @param pictureDto the DTO containing the updated picture data
     * @return the updated {@link PictureLightDto}
     * @throws RuntimeException if the picture with the given ID does not exist
     */
    @Transactional
    public PictureLightDto updatePicture(Integer id, PictureLightDto pictureDto) {
        LOGGER.info("Update picture with id {}", id);

        Picture picture = pictureDao.findPictureById(id)
                .orElseThrow(() -> new RuntimeException("Picture not found"));

        if (Boolean.TRUE.equals(pictureDto.getIsMain())) {
            List<Picture> otherPictures = pictureDao.findByIdMangasIdAndIsMainTrue(
                    picture.getIdMangas().getId());

            for (Picture other : otherPictures) {
                if (!other.getId().equals(picture.getId())) {
                    other.setMain(false);
                    pictureDao.save(other);
                }
            }
            picture.setMain(true);
        } else {
            picture.setMain(false);
        }

        picture.setUrl(pictureDto.getUrl());
        pictureDao.save(picture);

        return pictureMapper.toPictureLightDto(picture);
    }

    /**
     * Deletes a picture by its ID.
     * Removes the picture from the associated manga's pictures collection if
     * present.
     *
     * @param id the ID of the picture to delete
     * @throws EntityNotFoundException if no picture is found with the given ID
     */
    @Transactional
    public void deletePictureById(Integer id) {
        LOGGER.info("deletePictureById");
        Picture picture = pictureDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Picture with id " + id + " not found"));

        Manga manga = picture.getIdMangas();

        if (manga != null) {
            manga.getPictures().remove(picture);
            mangaDao.save(manga);
        }
        pictureDao.delete(picture);
    }
}
