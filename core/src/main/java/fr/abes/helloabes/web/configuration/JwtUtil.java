package fr.abes.helloabes.web.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service utilitaire pour les jetons JWT.
 * Cette classe est basée sur le framework Spring avec le module Spring Security.
 * @since 0.0.1
 * @author Duy Tran
 */
@Service
public class JwtUtil {

    /** Clé privée de cryptage des jetons JWT. */
    @Value("${secret.key}")
    private String secret ;

    /**
     * Vérifie si le jeton JWT est expiré.
     * @param token Jeton JWT en chaîne de caractère.
     * @return Vrai si le jeton est expiré, Faux sinon.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Génère un nouveau jeton JWT.
     * @param claims champs standard JWT.
     * @param subject Sujet (sub) du jeton JWT en chaîne de caractère.
     * @return Jeton JWT en chaîne de caractère.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Header header = Jwts.header();
        header.setType("JWT");
        return Jwts.builder().setClaims(claims).setSubject(subject).setHeader((Map<String, Object>)
                header).setIssuer("ABES").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    /**
     * Extrait les champs standard JWT (claims) d'un jeton JWT.
     * @param token Jeton JWT en chaîne de caractère.
     * @return Champs standard JWT (claims).
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Extrait le sujet (sub) d'un jeton JWT.
     * @param token Jeton JWT en chaîne de caractère.
     * @return Le sujet du jeton en chaîne de caractère.
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait la date d'expiration (exp) d'un jeton JWT.
     * @param token Jeton JWT en chaîne de caractère.
     * @return Date d'expiration
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Génère un jeton JWT à partir d'un sujet.
     * @param username Sujet (sub) du jeton JWT.
     * @return Jeton JWT en chaîne de caractère.
     */
    public String generateToken(String username) {
        Map<String, Object> claims;
        claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Vérifie la validité du jeton JWT avec :
     * <ul>
     *     <li>Le sujet du jeton doit correspondre au nom d'utilisateur.</li>
     *     <li>Le jeton ne doit pas être expiré.</li>
     * </ul>
     * @param token Jeton JWT en chaîne de caractère.
     * @param userDetails Utilisateur dans le framework Spring Security.
     * @return Vrai si le nom d'utilisateur et le sujet du jeton sont égaux et si le jeton n'est pas expiré, Faux sinon.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractSubject(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



}
