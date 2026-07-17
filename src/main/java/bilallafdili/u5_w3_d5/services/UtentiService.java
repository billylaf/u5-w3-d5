package bilallafdili.u5_w3_d5.services;

import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.enums.RuoloUtente;
import bilallafdili.u5_w3_d5.exceptions.BadRequestException;
import bilallafdili.u5_w3_d5.exceptions.NotFoundException;
import bilallafdili.u5_w3_d5.payloads.UtentePayload;
import bilallafdili.u5_w3_d5.repositories.UtentiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UtentiService {
    private final UtentiRepository utentiRepository;
    private final PasswordEncoder bcrypt;

    public UtentiService(UtentiRepository utentiRepository, PasswordEncoder bcrypt) {
        this.utentiRepository = utentiRepository;
        this.bcrypt = bcrypt;
    }

    public List<Utente> findAll() {
        return this.utentiRepository.findAll();
    }

    public Utente save(UtentePayload body) {
        if (this.utentiRepository.existsByEmail(body.getEmail()))
            throw new BadRequestException("email " + body.getEmail() + " è già utilizzata");

        if (this.utentiRepository.existsByUsername(body.getUsername()))
            throw new BadRequestException("username " + body.getUsername() + " è già utilizzato");

        RuoloUtente ruolo = body.getRuolo() != null ? body.getRuolo() : RuoloUtente.UTENTE_NORMALE;

        Utente newUtente = new Utente(
                body.getUsername(),
                body.getNome(),
                body.getCognome(),
                body.getEmail(),
                this.bcrypt.encode(body.getPassword()),
                ruolo
        );

        Utente saved = this.utentiRepository.save(newUtente);
        log.info("Utente con id " + saved.getId() + " creato con ruolo " + ruolo);
        return saved;
    }

    public Utente findById(UUID id) {
        return this.utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Utente findByEmail(String email) {
        return this.utentiRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("l'utente con email " + email + " non è stato trovato"));
    }
}