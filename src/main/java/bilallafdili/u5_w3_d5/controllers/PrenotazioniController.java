package bilallafdili.u5_w3_d5.controllers;

import bilallafdili.u5_w3_d5.entities.Prenotazione;
import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.payloads.PrenotazionePayload;
import bilallafdili.u5_w3_d5.services.PrenotazioniService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    private final PrenotazioniService prenotazioniService;

    public PrenotazioniController(PrenotazioniService prenotazioniService) {
        this.prenotazioniService = prenotazioniService;
    }

    @GetMapping
    public List<Prenotazione> findAll() {
        return this.prenotazioniService.findAll();
    }

    @GetMapping("/mie")
    public List<Prenotazione> findMiePrenotazioni(@AuthenticationPrincipal Utente utente) {
        return this.prenotazioniService.findMiePrenotazioni(utente);
    }

    @GetMapping("/{id}")
    public Prenotazione findById(@PathVariable UUID id) {
        return this.prenotazioniService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione create(@RequestBody @Valid PrenotazionePayload body,
                               BindingResult validationResult,
                               @AuthenticationPrincipal Utente utente) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            throw new ValidationException(String.valueOf(errorsList));
        }
        return this.prenotazioniService.save(body, utente);
    }

    @PatchMapping("/{id}/annulla")
    public Prenotazione annullaPrenotazione(@PathVariable UUID id, @AuthenticationPrincipal Utente utente) {
        return this.prenotazioniService.annullaPrenotazione(id, utente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal Utente utente) {
        this.prenotazioniService.findByIdAndDelete(id, utente);
    }
}
