package manga_up.manga_up.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class SameSiteCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        filterChain.doFilter(request, response);

        Collection<String> headers = response.getHeaders("Set-Cookie");
        boolean firstHeader = true;
        for (String header : headers) {
            if (header.startsWith("XSRF-TOKEN")) {
                StringBuilder newHeader = new StringBuilder(header);

                // On ajoute SameSite=None et Secure si ce n'est pas déjà présent
                if (!header.toLowerCase().contains("samesite")) {
                    newHeader.append("; SameSite=None");
                }
                if (!header.toLowerCase().contains("secure")) {
                    newHeader.append("; Secure");
                }

                if (firstHeader) {
                    response.setHeader("Set-Cookie", newHeader.toString());
                    firstHeader = false;
                } else {
                    response.addHeader("Set-Cookie", newHeader.toString());
                }
            }
        }
    }
}
