package bilallafdili.u5_w3_d5.services;

import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.exceptions.UnauthorizedException;
import bilallafdili.u5_w3_d5.payloads.LoginPayload;
import bilallafdili.u5_w3_d5.security.JWTTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UtentiService utentiService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UtentiService utentiService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.utentiService = utentiService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginPayload body) {
        Utente found = this.utentiService.findByEmail(body.email());

        if (this.bcrypt.matches(body.password(), found.getPassword())) {
            return this.jwtTools.generateToken(found);
        } else {
            throw new UnauthorizedException("Credenziali sbagliate");
        }
    }
}