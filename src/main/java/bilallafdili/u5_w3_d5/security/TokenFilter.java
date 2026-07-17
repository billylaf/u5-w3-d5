package bilallafdili.u5_w3_d5.security;

import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.exceptions.UnauthorizedException;
import bilallafdili.u5_w3_d5.services.UtentiService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JWTTools jwtTools;
    private final UtentiService utentiService;

    public TokenFilter(JWTTools jwtTools, UtentiService utentiService) {
        this.jwtTools = jwtTools;
        this.utentiService = utentiService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("inserire il token nell authorization header nel formato Bearer ");

        String accessToken = authHeader.replace("Bearer ", "");
        this.jwtTools.verifyToken(accessToken);
        UUID utenteId = this.jwtTools.extractIdFromToken(accessToken);
        Utente authenticatedUtente = this.utentiService.findById(utenteId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUtente, null, authenticatedUtente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}