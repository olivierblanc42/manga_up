package manga_up.manga_up.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.dao.AddressDao;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.projection.appUser.AppUserLittleProjection;
import manga_up.manga_up.projection.userAdress.UserAddressProjection;
import manga_up.manga_up.service.CustomUserDetailsService;
import manga_up.manga_up.service.UserAddressService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AddressController.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private AddressDao addressDao;
    @MockitoBean
    private UserAddressService userAddressService;

    @MockitoBean
    private JwtUtils jwtUtils;

    private static class AppUserLittleProjectionTest implements AppUserLittleProjection {
        private final Integer id;
        private final String username;
        private final String firstname;
        private final String lastname;

        public AppUserLittleProjectionTest(Integer id, String username, String firstname, String lastname) {
            this.id = id;
            this.username = username;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getFirstname() {
            return firstname;
        }

        @Override
        public String getLastname() {
            return lastname;
        }
    }

    private static class UserAddressProjectionTest implements UserAddressProjection {
        private final Integer id;
        private final String line1;
        private final String line2;
        private final String line3;
        private final String city;
        private final LocalDateTime createdAt;
        private final String postalCode;
        private final Set<AppUserLittleProjection> appUsers;

        public UserAddressProjectionTest(Integer id, String line1, String line2, String line3, String city,
                LocalDateTime createdAt, String postalCode,
                Set<AppUserLittleProjection> appUsers) {
            this.id = id;
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
            this.city = city;
            this.createdAt = createdAt;
            this.postalCode = postalCode;
            this.appUsers = appUsers;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getLine1() {
            return line1;
        }

        @Override
        public String getLine2() {
            return line2;
        }

        @Override
        public String getLine3() {
            return line3;
        }

        @Override
        public String getCity() {
            return city;
        }

        @Override
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        @Override
        public String getPostalCode() {
            return postalCode;
        }

        @Override
        public Set<AppUserLittleProjection> getAppUsers() {
            return appUsers;
        }
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnAllAddress() throws Exception {
        AppUserLittleProjection user1 = new AppUserLittleProjectionTest(1, "jdoe", "John", "Doe");
        AppUserLittleProjection user2 = new AppUserLittleProjectionTest(2, "asmith", "Alice", "Smith");

        Set<AppUserLittleProjection> usersSet1 = new HashSet<>(Set.of(user1));
        Set<AppUserLittleProjection> usersSet2 = new HashSet<>(Set.of(user1, user2));

        UserAddressProjection address1 = new UserAddressProjectionTest(
                100,
                "123 Rue Principale",
                "Bat A",
                "Étage 3",
                "Paris",
                LocalDateTime.of(2023, 5, 10, 14, 30),
                "75001",
                usersSet1);

        UserAddressProjection address2 = new UserAddressProjectionTest(
                101,
                "456 Avenue des Champs",
                null,
                null,
                "Lyon",
                LocalDateTime.of(2024, 2, 18, 9, 15),
                "69001",
                usersSet2);

        List<UserAddressProjection> address = new ArrayList<>();
        address.add(address1);
        address.add(address2);
        Page<UserAddressProjection> page = new PageImpl<>(address);

        when(userAddressService.findAllByPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/addresses")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].line1").value("123 Rue Principale"))
                .andExpect(jsonPath("$.content[1].line1").value("456 Avenue des Champs"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnAddressByid() throws Exception {
        AppUserLittleProjection user1 = new AppUserLittleProjectionTest(1, "jdoe", "John", "Doe");

        Set<AppUserLittleProjection> usersSet1 = new HashSet<>(Set.of(user1));

        UserAddressProjection address1 = new UserAddressProjectionTest(
                100,
                "123 Rue Principale",
                "Bat A",
                "Étage 3",
                "Paris",
                LocalDateTime.of(2023, 5, 10, 14, 30),
                "75001",
                usersSet1);

        when(userAddressService.findById(1)).thenReturn(address1);
        mockMvc.perform(get("/api/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.line1").value("123 Rue Principale"));

    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnCreatedAuthor() throws Exception {
        String json = """
                {
                  "line1": "12 rue de la Paix",
                  "line2": "Bâtiment A",
                  "line3": "2ème étage",
                  "city": "Paris",
                  "createdAt": "2025-06-05T16:09:22.331Z",
                  "postalCode": "75001"
                }
                """;

        UserAddressDto address = new UserAddressDto(
                "12 rue de la Paix",
                "Bâtiment A",
                "2ème étage",
                "Paris",
                Instant.parse("2025-06-05T16:09:22.331Z"),
                "75001");

        when(userAddressService.save(any())).thenReturn(address);

        mockMvc.perform(post("/api/addresses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.line1").value("12 rue de la Paix"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnUpdateAuthor() throws Exception {
        String json = """
                {
                  "line1": "12 rue de la Paix",
                  "line2": "Bâtiment A",
                  "line3": "2ème étage",
                  "city": "Paris",
                  "createdAt": "2025-06-05T16:09:22.331Z",
                  "postalCode": "75001"
                }
                """;

        UserAddressDto address = new UserAddressDto(
                "13 rue de la Paix",
                "Bâtiment A",
                "2ème étage",
                "Paris",
                Instant.parse("2025-06-05T16:09:22.331Z"),
                "75001");

        when(userAddressService.updateUserAddress(eq(1), any(UserAddressDto.class))).thenReturn(address);

        mockMvc.perform(put("/api/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.line1").value("13 rue de la Paix"));
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldReturnInternalServerErrorWhenUpdateFails() throws Exception {
        String json = """
                {
                  "line1": "12 rue de la Paix",
                  "line2": "Bâtiment A",
                  "line3": "2ème étage",
                  "city": "Paris",
                  "createdAt": "2025-06-05T16:09:22.331Z",
                  "postalCode": "75001"
                }
                """;

        when(userAddressService.updateUserAddress(eq(1), any(UserAddressDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/api/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user", roles = { "ADMIN" })
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/addresses/1").with(csrf()))
                .andExpect(status().isOk());

        verify(userAddressService).deleteUserAddress(1);
    }
}
