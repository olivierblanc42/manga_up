package manga_up.manga_up.dao;

import manga_up.manga_up.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);
}
