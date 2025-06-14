package manga_up.manga_up.configuration;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Method;
import java.security.Key;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

 
        setField(jwtUtils, "secretKey", "01234567890123456789012345678901");
        setField(jwtUtils, "expirationTime", 1000 * 60 * 60L);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = JwtUtils.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Key getSignKey() {
        try {
            Method method = JwtUtils.class.getDeclaredMethod("getSignKey");
            method.setAccessible(true);
            return (Key) method.invoke(jwtUtils);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void generateToken_shouldCreateValidJwt() {
        String username = "user1";
        String role = "ROLE_USER";

        String token = jwtUtils.generateToken(username, role);
        assertNotNull(token);

        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals(username, extractedUsername);

        List<String> roles = jwtUtils.extractRoles(token);
        assertEquals(1, roles.size());
        assertEquals(role, roles.get(0));
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String username = "user1";
        String role = "ROLE_USER";

        String token = jwtUtils.generateToken(username, role);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        boolean valid = jwtUtils.validateToken(token, userDetails);
        assertTrue(valid);
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
        JwtUtils shortExpiryJwtUtils = new JwtUtils();
        setField(shortExpiryJwtUtils, "secretKey", "01234567890123456789012345678901");
        setField(shortExpiryJwtUtils, "expirationTime", 100L);

        String token = shortExpiryJwtUtils.generateToken("user1", "ROLE_USER");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user1");

        Thread.sleep(200); 

        boolean valid = shortExpiryJwtUtils.validateToken(token, userDetails);
        assertFalse(valid);
    }

    @Test
    void extractRoles_shouldReturnEmptyList_whenNoRoles() {
        String token = Jwts.builder()
                .setSubject("user1")
                .signWith(getSignKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();

        List<String> roles = jwtUtils.extractRoles(token);
        assertTrue(roles.isEmpty());
    }

    @Test
    void extractUsername_shouldThrowException_forInvalidToken() {
        String invalidToken = "invalid.token.here";
        assertThrows(Exception.class, () -> jwtUtils.extractUsername(invalidToken));
    }
}
