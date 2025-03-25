package manga_up.manga_up.dao;

import manga_up.manga_up.model.GenderUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderUserDao extends JpaRepository<GenderUser, Integer> {
}
