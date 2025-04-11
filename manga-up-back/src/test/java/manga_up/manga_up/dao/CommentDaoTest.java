package manga_up.manga_up.dao;

import manga_up.manga_up.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CommentDaoTest {

   @Autowired
   private CommentDao commentDao;
   @Autowired
   private UserDao userDao;
   @Autowired
   private GenderUserDao genderUserDao;
   @Autowired
   private AddressDao addressDao;
   @Autowired
   private CategoryDao categoryDao;
   @Autowired
   private MangaDao mangaDao;

   @Test
   void shouldFindAll() {
      List<Comment> comments = commentDao.findAll();

      assertEquals(2, comments.size());
      assertEquals(5,comments.get(0).getRating());
   }

   @Test
   void shouldFindById() {
      Comment comment = commentDao.findById(1).get();
      assertEquals(5,comment.getRating());
   }

   @Test
   void shouldSave() {

      GenderUser genderUser = new GenderUser();
      genderUser.setLabel("Gender Fluid");
      GenderUser saveGenderUser = genderUserDao.save(genderUser);

      UserAddress userAddress = new UserAddress();
      userAddress.setLine1("31 rue des Coquelicots");
      userAddress.setLine2("Résidence Les Jardins");
      userAddress.setLine3("Appartement 5B");
      userAddress.setCity("Montpellier");
      userAddress.setPostalCode("34000");
      UserAddress savedAddress = addressDao.save(userAddress);
      AppUser appUser = new AppUser();
      appUser.setUsername("johndoe2");
      appUser.setFirstname("John");
      appUser.setLastname("Doe2");
      appUser.setRole("ROLE_USER");
      appUser.setPhoneNumber("0123456789");
      appUser.setPassword("123456");
      appUser.setEmail("johndoe2@gmail.com");
      appUser.setCreatedAt(Instant.parse("2023-04-10T10:00:00Z"));
      appUser.setIdGendersUser(saveGenderUser);
      appUser.setIdUserAddress(savedAddress);
      AppUser savedAppUser = userDao.save(appUser);

      Category category = new Category();
      category.setLabel("Manga");
      category.setDescription("Description");
      category.setCreatedAt(Instant.parse("2023-04-10T10:00:00Z"));
      category = categoryDao.save(category);

      Manga manga = new Manga();
      manga.setTitle("Dragon Ball Z");
      manga.setSubtitle("Un voyage épique plus épique");
      manga.setReleaseDate(Instant.parse("2023-04-10T10:00:00Z"));
      manga.setSummary("L’histoire de Goku.");
      manga.setPrice(BigDecimal.valueOf(20.00));
      manga.setPriceHt(BigDecimal.valueOf(18.00));
      manga.setActive(true);
      manga.setInStock(true);
      manga.setIdCategories(category);

      Manga savedManga = mangaDao.save(manga);

      Comment comment = new Comment();
      comment.setRating(5);
      comment.setCreatedAt(Instant.parse("2023-04-10T10:00:00Z"));
      comment.setComment("This is a comment");
      comment.setIdMangas(savedManga);
      comment.setIdUsers(savedAppUser);

    Comment saveComment =  commentDao.save(comment);
      assertNotNull(saveComment.getId());
      assertEquals(5,saveComment.getRating());
   }


   @Test
   void shouldDelete() {
      commentDao.delete(commentDao.findById(1).get());

      Optional<Comment> comment = commentDao.findById(1);

      assertFalse(comment.isPresent());
   }

}