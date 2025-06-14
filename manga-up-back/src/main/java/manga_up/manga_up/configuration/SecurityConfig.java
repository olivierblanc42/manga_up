package manga_up.manga_up.configuration;

import manga_up.manga_up.filter.JwtFilter;
import manga_up.manga_up.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtils jwtUtils) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                            .csrf(AbstractHttpConfigurer::disable)
                            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                            .authorizeHttpRequests(auth -> auth
                                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()                                                      
                                            .requestMatchers(
                                                            "/swagger-ui/**",
                                                            "/v3/api-docs/**",
                                                            "/swagger-ui.html",
                                                            "/my-swagger/**",
                                                            "/my-api-docs/**",
                                                            "/api/auth/register",
                                                            "/api/auth/login",
                                                            "/api/public/**")
                                            .permitAll()
                                            .anyRequest().authenticated())
                            .addFilterBefore(new JwtFilter(customUserDetailsService, jwtUtils),
                                            UsernamePasswordAuthenticationFilter.class)
                            .build();
    }
    
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);

        
        corsConfiguration.addAllowedOrigin("https://mangaup-production.up.railway.app");
        corsConfiguration.addAllowedOrigin("https://manga-up.onrender.com");
        corsConfiguration.addAllowedOrigin("http://localhost:4200");

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
