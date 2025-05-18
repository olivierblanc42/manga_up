package manga_up.manga_up.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manga_up.manga_up.configuration.JwtUtils;
import manga_up.manga_up.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    public JwtFilter(CustomUserDetailsService customUserDetailsService, JwtUtils jwtUtils) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("Request URI: " + requestURI);

        if (requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/webjars") ||
                requestURI.startsWith("/api/public")) {
            System.out.println("Request excluded from JWT filtering: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // Lecture du cookie JWT
        String jwt = null;
        if (request.getCookies() == null) {
            System.out.println("No cookies found");
        } else {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("Cookie found: " + cookie.getName() + "=" + cookie.getValue());
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    System.out.println("JWT extracted from cookie");
                }
            }
        }

        if (jwt == null) {
            System.out.println("No JWT token present in cookies");
        }

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String username = jwtUtils.extractUsername(jwt);
                System.out.println("Extracted username from JWT: " + username);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (jwtUtils.validateToken(jwt, userDetails)) {
                    System.out.println("JWT token validated");
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println("Security context set with authentication for user: " + username);
                } else {
                    System.out.println("JWT token validation failed");
                }
            } catch (Exception e) {
                System.out.println("Exception during JWT token validation: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("JWT token is null or authentication already set");
        }

        filterChain.doFilter(request, response);
    }
    
}
