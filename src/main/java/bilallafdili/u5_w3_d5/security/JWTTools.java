package bilallafdili.u5_w3_d5.security;

import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {
    private final String secret;

    public JWTTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(Utente utente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .subject(String.valueOf(utente.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("ci sono stati problemi con il token, rieffettuare login");
        }
    }

    public UUID extractIdFromToken(String token) {
        return UUID.fromString(
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build()
                        .parseSignedClaims(token).getPayload().getSubject()
        );
    }
}