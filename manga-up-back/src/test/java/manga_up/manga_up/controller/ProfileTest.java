package manga_up.manga_up.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ProfileTest {

    @Autowired
    private Environment env;

    @Test
    void printActiveProfiles() {
        String[] activeProfiles = env.getActiveProfiles();
        System.out.println("Active Spring profiles: " + String.join(", ", activeProfiles));
        assertThat(activeProfiles).contains("test");    }
}
