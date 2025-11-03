package fr.abes.helloabes.web.security;

import fr.abes.helloabes.core.service.CustomUserDetailsService;
import fr.abes.helloabes.web.configuration.JwtAuthenticationEntryPoint;
import fr.abes.helloabes.web.configuration.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * La classe {@code SecurityConfigurer} permet de configurer la sécurité du service web.
 * Cette classe est basée sur le framework Spring avec le module Spring Security.
 * @since 0.0.1
 * @author Duy Tran
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    /**
     * Filtre pour les jetons JWT.
     */
    private final JwtAuthenticationFilter jwtFilter;

    /**
     * Construit une nouvelle configuration de sécurité pour le service web avec un filtre pour les jetons JWT.
     * @param jwtFilter Filtre pour les jetons JWT.
     */
    public SpringSecurityConfig(JwtAuthenticationFilter jwtFilter, JwtAuthenticationEntryPoint unauthorizedHandler, CustomUserDetailsService customUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.unauthorizedHandler = unauthorizedHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Configure la politique de sécurité du service web
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain securedFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .securityMatcher("/api/v1/secured/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionHandling)-> exceptionHandling.authenticationEntryPoint(unauthorizedHandler))
                .httpBasic(withDefaults())
                .formLogin(Customizer.withDefaults());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers( "/api/v1", "/api/v1/register", "/api/v1/login").permitAll()
                        .requestMatchers("/swagger-ui**", "/swagger-ui/**", "/api-docs**", "/api-docs/**").permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionHandling)-> exceptionHandling.authenticationEntryPoint(unauthorizedHandler))
                .httpBasic(withDefaults())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Réglages des CORS (très ouverts) qui permettent au front en javascript (abes-hello-front) de s'y connecter
     * depuis n'importe quel domaine : localhost, dev, test, prod, ou même depuis d'autres domaines.
     */
    @Bean
    public FilterRegistrationBean simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}