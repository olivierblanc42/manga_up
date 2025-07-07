package manga_up.manga_up.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;

public class SameSiteCookieCsrfTokenRepository {

    private final CookieCsrfTokenRepository delegate = CookieCsrfTokenRepository.withHttpOnlyFalse();

    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        HttpServletResponseWrapper wrapped = new HttpServletResponseWrapper(response) {
            @Override
            public void addHeader(String name, String value) {
                if ("Set-Cookie".equalsIgnoreCase(name) && value.startsWith(delegate.getCookiePath())) {
                    if (!value.toLowerCase().contains("samesite")) {
                        value += "; SameSite=None";
                    }
                    if (!value.toLowerCase().contains("secure")) {
                        value += "; Secure";
                    }
                }
                super.addHeader(name, value);
            }

            @Override
            public void setHeader(String name, String value) {
                if ("Set-Cookie".equalsIgnoreCase(name) && value.startsWith(delegate.getCookiePath())) {
                    if (!value.toLowerCase().contains("samesite")) {
                        value += "; SameSite=None";
                    }
                    if (!value.toLowerCase().contains("secure")) {
                        value += "; Secure";
                    }
                }
                super.setHeader(name, value);
            }
        };

        delegate.setSecure(true);
        delegate.setCookieHttpOnly(false);

        delegate.saveToken(token, request, wrapped);
    }
}
