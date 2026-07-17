package bilallafdili.u5_w3_d5.services;

import bilallafdili.u5_w3_d5.entities.Evento;
import bilallafdili.u5_w3_d5.entities.Prenotazione;
import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.enums.StatoEvento;
import bilallafdili.u5_w3_d5.enums.StatoPrenotazione;
import bilallafdili.u5_w3_d5.exceptions.BadRequestException;
import bilallafdili.u5_w3_d5.exceptions.NotFoundException;
import bilallafdili.u5_w3_d5.exceptions.UnauthorizedException;
import bilallafdili.u5_w3_d5.payloads.PrenotazionePayload;
import bilallafdili.u5_w3_d5.repositories.PrenotazioniRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PrenotazioniService {
    private final PrenotazioniRepository prenotazioniRepository;
    private final EventiService eventiService;

    public PrenotazioniService(PrenotazioniRepository prenotazioniRepository, EventiService eventiService) {
        this.prenotazioniRepository = prenotazioniRepository;
        this.eventiService = eventiService;
    }

    public List<Prenotazione> findAll() {
        return this.prenotazioniRepository.findAll();
    }

    public List<Prenotazione> findMiePrenotazioni(Utente utente) {
        return this.prenotazioniRepository.findByUtenteAndStato(utente, StatoPrenotazione.CONFERMATA);
    }

    public Prenotazione save(PrenotazionePayload body, Utente utente) {
        Evento evento = this.eventiService.findById(body.getEventoId());

        if (evento.getStato() != StatoEvento.PROGRAMMATO)
            throw new BadRequestException("Evento non disponibile");
        if (evento.getPostiDisponibili() <= 0)
            throw new BadRequestException("Posti esauriti");
        if (this.prenotazioniRepository.existsByUtenteAndEventoAndStato(utente, evento, StatoPrenotazione.CONFERMATA))
            throw new BadRequestException("Hai già prenotato questo evento");

        Prenotazione saved = this.prenotazioniRepository.save(new Prenotazione(evento, utente));

        evento.setPostiDisponibili(evento.getPostiDisponibili() - 1);
        if (evento.getPostiDisponibili() == 0)
            evento.setStato(StatoEvento.COMPLETATO);
        this.eventiService.eventiRepository.save(evento);

        log.info("Prenotazione creata con id {} per evento {}", saved.getId(), evento.getTitolo());
        return saved;
    }

    public Prenotazione findById(UUID id) {
        return this.prenotazioniRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional
    public Prenotazione annullaPrenotazione(UUID id, Utente utente) {
        Prenotazione found = this.findById(id);

        if (!found.getUtente().getId().equals(utente.getId()))
            throw new UnauthorizedException("Non autorizzato");
        if (found.getStato() == StatoPrenotazione.ANNULLATA)
            throw new BadRequestException("Prenotazione già annullata");

        found.setStato(StatoPrenotazione.ANNULLATA);

        Evento evento = found.getEvento();
        evento.setPostiDisponibili(evento.getPostiDisponibili() + 1);
        if (evento.getStato() == StatoEvento.COMPLETATO && evento.getPostiDisponibili() > 0)
            evento.setStato(StatoEvento.PROGRAMMATO);
        this.eventiService.eventiRepository.save(evento);

        log.info("Prenotazione annullata con id {}", id);
        return this.prenotazioniRepository.save(found);
    }

    @Transactional
    public void findByIdAndDelete(UUID id, Utente utente) {
        Prenotazione found = this.findById(id);

        if (!found.getUtente().getId().equals(utente.getId()))
            throw new UnauthorizedException("Non autorizzato");
        if (found.getStato() == StatoPrenotazione.ANNULLATA)
            throw new BadRequestException("Prenotazione già annullata");

        Evento evento = found.getEvento();
        evento.setPostiDisponibili(evento.getPostiDisponibili() + 1);
        if (evento.getStato() == StatoEvento.COMPLETATO && evento.getPostiDisponibili() > 0)
            evento.setStato(StatoEvento.PROGRAMMATO);
        this.eventiService.eventiRepository.save(evento);

        this.prenotazioniRepository.delete(found);
        log.info("Prenotazione eliminata con id {}", id);
    }
}
