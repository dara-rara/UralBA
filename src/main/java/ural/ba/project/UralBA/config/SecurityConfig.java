package ural.ba.project.UralBA.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ural.ba.project.UralBA.seceruty.JwtFilter;
import ural.ba.project.UralBA.seceruty.JwtProvider;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Конфигурационный класс, отвечающий за настройку безопасности приложения
 * Определяет правила доступа, управление сессиями, CORS и добавляет кастомный JWT фильтр
 *
 * @author Daria
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtProvider jwtProvider,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtFilter = new JwtFilter(jwtProvider, handlerExceptionResolver);
    }

    /**
     * Определяет цепочку фильтров безопасности (Security Filter Chain)
     * Настраивает отключение CSRF, политику сессий STATELESS, правила авторизации запросов
     * и регистрирует JwtFilter перед стандартным фильтром аутентификации по логину/паролю
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**", "/user/create").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Настраивает конфигурацию Cross-Origin Resource Sharing (CORS)
     * Определяет разрешенные источники (origins), HTTP-методы и заголовки
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173", "https://dianasuf-community-analysts-aecb.twc1.net"
        )); // Разрешенные origin
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        )); // Разрешенные методы
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "X-Refresh-Token"
        )); // Разрешенные заголовки
        configuration.setExposedHeaders(Arrays.asList(
                "Set-Cookie",      // Важно для cookies!
                "Authorization"
        ));
        configuration.setAllowCredentials(true); // Разрешить передачу учетных данных

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Применить настройки ко всем путям
        return source;
    }

    /**
     * Предоставляет бин AuthenticationManager для управления процессами аутентификации.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}