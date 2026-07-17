package bilallafdili.u5_w3_d5.controllers;

import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.payloads.LoginPayload;
import bilallafdili.u5_w3_d5.payloads.LoginResponsePayload;
import bilallafdili.u5_w3_d5.payloads.UtentePayload;
import bilallafdili.u5_w3_d5.services.AuthService;
import bilallafdili.u5_w3_d5.services.UtentiService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UtentiService utentiService;

    public AuthController(AuthService authService, UtentiService utentiService) {
        this.authService = authService;
        this.utentiService = utentiService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente register(@RequestBody @Valid UtentePayload body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            throw new ValidationException(String.valueOf(errorsList));
        }
        return this.utentiService.save(body);
    }

    @PostMapping("/login")
    public LoginResponsePayload login(@RequestBody @Valid LoginPayload body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            throw new ValidationException(String.valueOf(errorsList));
        }
        String token = this.authService.checkCredentialsAndGenerateToken(body);
        return new LoginResponsePayload(token);
    }
}
