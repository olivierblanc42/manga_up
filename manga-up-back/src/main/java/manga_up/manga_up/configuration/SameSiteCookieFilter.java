package manga_up.manga_up.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SameSiteCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        filterChain.doFilter(request, response);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("XSRF-TOKEN".equals(cookie.getName())) {
                    // Re-créer le cookie avec SameSite=None; Secure
                    StringBuilder cookieValue = new StringBuilder();
                    cookieValue.append(cookie.getName()).append("=").append(cookie.getValue());
                    cookieValue.append("; Path=").append(cookie.getPath() != null ? cookie.getPath() : "/");
                    cookieValue.append("; Secure");
                    cookieValue.append("; HttpOnly=false");
                    cookieValue.append("; SameSite=None");

                    response.setHeader("Set-Cookie", cookieValue.toString());
                }
            }
        }
    }
}
