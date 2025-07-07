package manga_up.manga_up.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SameSiteCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        filterChain.doFilter(request, response);

        Collection<String> headers = response.getHeaders("Set-Cookie");
        List<String> newHeaders = new ArrayList<>();

        for (String header : headers) {
            if (header.startsWith("XSRF-TOKEN")) {
                StringBuilder newHeader = new StringBuilder(header);
                if (!header.toLowerCase().contains("samesite")) {
                    newHeader.append("; SameSite=None");
                }
                if (!header.toLowerCase().contains("secure")) {
                    newHeader.append("; Secure");
                }
                newHeaders.add(newHeader.toString());
            } else {
                newHeaders.add(header);
            }
        }

        response.setHeader("Set-Cookie", null); // Supprime tous les Set-Cookie existants
        for (String header : newHeaders) {
            response.addHeader("Set-Cookie", header);
        }
    }
}
