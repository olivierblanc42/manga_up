package manga_up.manga_up.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import manga_up.manga_up.dao.AuthorDao;
import manga_up.manga_up.dao.StatusDao;
import manga_up.manga_up.projection.author.AuthorProjection;
import manga_up.manga_up.projection.status.StatusProjection;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class StatusServiceTest {

@InjectMocks
    private StatusService statusService;
 @Mock
    private StatusDao statusDao;

    private static class StatusProjectionTest implements StatusProjection {
        private final Integer id;
        private final String label;

        public StatusProjectionTest(Integer id,String label) {
            this.id = id;
            this.label = label;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String getLabel() {
            return label;
        }    


}

@Test
void shouldReturnAllStatus() {
     Pageable pageable = PageRequest.of(0, 5);

     StatusProjection status1 = new StatusProjectionTest(1,"null");
     StatusProjection status2 = new StatusProjectionTest(2, "Envoyé");
     Page<StatusProjection> page = new PageImpl<>(List.of(status1, status2));
     when(statusDao.findAllByPage(pageable)).thenReturn(page);

     Page<StatusProjection> result = statusService.findAllByPage(pageable);
     assertThat(result).hasSize(2).containsExactly(status1, status2);
}

}