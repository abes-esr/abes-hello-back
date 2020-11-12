package fr.abes.helloabes.web.security;

import fr.abes.helloabes.core.configuration.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * La classe {@code SecurityConfigurer} permet de configurer la sécurité du service web.
 * Cette classe est basée sur le framework Spring avec le module Spring Security.
 * @since 0.0.1
 * @author Duy Tran
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    /**
     * Filtre pour les jetons JWT.
     */
    private final JwtFilter jwtFilter;

    /**
     * Construit une nouvelle configuration de sécurité pour le service web avec un filtre pour les jetons JWT.
     * @param jwtFilter Filtre pour les jetons JWT.
     */
    @Autowired
    public SecurityConfigurer(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Permet de configurer la politique de sécurité du service web tel que :
     * <ul>
     *     <li>Aucune session HTTP n'est créée pour la couche Spring Security.</li>
     *     <li>Toutes les routes derrières /secured/ nécessite une authentification.</li>
     *     <li>Les exceptions levées par les filtres sont gérées par le framework Spring Security.
     *     <p> Note : les filtres sont exécutés avant les contrôlleurs. Si un filtre de sécurité lève une exception,
     *     il est préférable de l'attraper pour la gérer dans Spring et non par le Servlet.</p></li>
     *     <li>Le filtre JWT {@link #jwtFilter} est ajouté avant le filtre d'authentification
     *      utilisateur / mot de passe du framework Spring Security.</li>
     * </ul>
     *
     * @param http
     * @throws Exception s'il y a une erreur dans la configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/secured/**").authenticated()
                .and().exceptionHandling()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }

}
