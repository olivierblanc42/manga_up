package manga_up.manga_up.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PublicController.class)
@ActiveProfiles("test")
class PublicControllerTest {
  
  @Autowired
  private MockMvc mockMvc;


}
