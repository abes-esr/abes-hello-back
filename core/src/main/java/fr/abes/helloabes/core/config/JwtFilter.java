package fr.abes.helloabes.core.config;

import fr.abes.helloabes.core.services.CustomUserDetailsService;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtre de jetons JWT.
 * Cette classe est basée sur le framework Spring avec les modules Spring Web et Spring Security.
 * @since 0.0.1
 * @author Duy Tran
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /** Service utilitaire pour les jetons JWT. */
    private final JwtUtil jwtUtil;

    /** Service d'authentification des utilisateurs. */
    private final CustomUserDetailsService service;

    /**
     * Construit un filtre de jetons JWT.
     * @param jwtUtil Service utilitaire pour les jetons JWT.
     * @param service Service d'authentification des utilisateurs.
     */
    @Autowired
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    /**
     * Execute le filtre avec la vérification du jeton JWT.
     * @param httpServletRequest requête HTTP entrante.
     * @param httpServletResponse requête HTTP sortante.
     * @param filterChain Chaîne de filtres.
     * @throws ServletException si
     * @throws IOException si
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");

        String token = null;
        String userName = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                userName = jwtUtil.extractUsername(token);
            } catch (SignatureException ex) {
                System.out.println("Invalid JWT Signature");
            } catch (MalformedJwtException ex){
                System.out.println("Invalid JWT token");
            }
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = service.loadUserByUsername(userName);

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
