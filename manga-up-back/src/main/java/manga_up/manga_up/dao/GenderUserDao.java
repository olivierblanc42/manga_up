package manga_up.manga_up.dao;

import manga_up.manga_up.dto.genderUser.GenderUserDto;
import manga_up.manga_up.model.GenderUser;
import manga_up.manga_up.projection.genderUser.GenderUserProjection;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link GenderUser} entities.
 * 
 */
@Repository
public interface GenderUserDao extends JpaRepository<GenderUser, Integer> {

  @Query("SELECT g FROM GenderUser g LEFT JOIN FETCH g.appUsers")
  Page<GenderUserProjection> getGenderUser(Pageable pageable);

  @Query("SELECT g FROM GenderUser g LEFT JOIN FETCH g.appUsers WHERE g.id = :genderUserId")
  Optional<GenderUser> findGenderById(@ParameterObject Integer genderUserId);

  @Query("SELECT g FROM GenderUser g LEFT JOIN FETCH g.appUsers WHERE g.id = :genderUserId")
  Optional<GenderUserProjection> findGenderUserProjectionById(@ParameterObject Integer genderUserId);


  @Query("SELECT new manga_up.manga_up.dto.genderUser.GenderUserDto(g.id, g.label) FROM GenderUser g")
  List<GenderUserDto> getGenderUserDto();


}
