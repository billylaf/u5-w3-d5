package bilallafdili.u5_w3_d5.controllers;

import bilallafdili.u5_w3_d5.entities.Evento;
import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.payloads.EventoPayload;
import bilallafdili.u5_w3_d5.services.EventiService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventiController {
    private final EventiService eventiService;

    public EventiController(EventiService eventiService) {
        this.eventiService = eventiService;
    }

    @GetMapping
    public List<Evento> findAll() {
        return this.eventiService.findAll();
    }

    @GetMapping("/disponibili")
    public List<Evento> findDisponibili() {
        return this.eventiService.findDisponibili();
    }

    @GetMapping("/miei")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public List<Evento> findMieiEventi(@AuthenticationPrincipal Utente organizzatore) {
        return this.eventiService.findMieiEventi(organizzatore);
    }

    @GetMapping("/{id}")
    public Evento findById(@PathVariable UUID id) {
        return this.eventiService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento create(@RequestBody @Valid EventoPayload body,
                         BindingResult validationResult,
                         @AuthenticationPrincipal Utente organizzatore) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            throw new ValidationException(String.valueOf(errorsList));
        }
        return this.eventiService.save(body, organizzatore);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento update(@PathVariable UUID id,
                         @RequestBody @Valid EventoPayload body,
                         BindingResult validationResult,
                         @AuthenticationPrincipal Utente organizzatore) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            throw new ValidationException(String.valueOf(errorsList));
        }
        return this.eventiService.findByIdAndUpdate(id, body, organizzatore);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal Utente organizzatore) {
        this.eventiService.findByIdAndDelete(id, organizzatore);
    }

    @PatchMapping("/{id}/cancella")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento cancellaEvento(@PathVariable UUID id, @AuthenticationPrincipal Utente organizzatore) {
        return this.eventiService.cancellaEvento(id, organizzatore);
    }
}
