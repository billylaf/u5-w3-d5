package bilallafdili.u5_w3_d5.services;

import bilallafdili.u5_w3_d5.entities.Evento;
import bilallafdili.u5_w3_d5.entities.Utente;
import bilallafdili.u5_w3_d5.enums.StatoEvento;
import bilallafdili.u5_w3_d5.exceptions.BadRequestException;
import bilallafdili.u5_w3_d5.exceptions.NotFoundException;
import bilallafdili.u5_w3_d5.exceptions.UnauthorizedException;
import bilallafdili.u5_w3_d5.payloads.EventoPayload;
import bilallafdili.u5_w3_d5.repositories.EventiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class EventiService {
    final EventiRepository eventiRepository;

    public EventiService(EventiRepository eventiRepository) {
        this.eventiRepository = eventiRepository;
    }

    public List<Evento> findAll() {
        return this.eventiRepository.findAll();
    }

    public List<Evento> findDisponibili() {
        return this.eventiRepository.findEventiDisponibili(StatoEvento.PROGRAMMATO);
    }

    public List<Evento> findMieiEventi(Utente organizzatore) {
        return this.eventiRepository.findByCreatoreAndStatoNot(organizzatore, StatoEvento.CANCELLATO);
    }

    public Evento save(EventoPayload body, Utente creatore) {
        if (body.getData().isBefore(LocalDateTime.now()))
            throw new BadRequestException("La data dell'evento deve essere futura");

        Evento newEvento = new Evento(
                body.getTitolo(),
                body.getDescrizione(),
                body.getData(),
                body.getLuogo(),
                body.getPostiTotali(),
                creatore
        );

        Evento saved = this.eventiRepository.save(newEvento);
        log.info("Evento con id {} creato da {}", saved.getId(), creatore.getUsername());
        return saved;
    }

    public Evento findById(UUID id) {
        return this.eventiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Evento findByIdAndUpdate(UUID id, EventoPayload body, Utente organizzatore) {
        Evento found = this.findById(id);

        if (!found.getCreatore().getId().equals(organizzatore.getId()))
            throw new UnauthorizedException("Non sei autorizzato a modificare questo evento");

        if (found.getStato() == StatoEvento.CANCELLATO)
            throw new BadRequestException("Non puoi modificare un evento cancellato");

        if (body.getData().isBefore(LocalDateTime.now()))
            throw new BadRequestException("La data dell'evento deve essere futura");

        found.setTitolo(body.getTitolo());
        found.setDescrizione(body.getDescrizione());
        found.setData(body.getData());
        found.setLuogo(body.getLuogo());

        if (body.getPostiTotali() != null && body.getPostiTotali() > 0) {
            int differenza = body.getPostiTotali() - found.getPostiTotali();
            found.setPostiTotali(body.getPostiTotali());
            found.setPostiDisponibili(found.getPostiDisponibili() + differenza);

            if (found.getPostiDisponibili() < 0)
                throw new BadRequestException("Il numero di posti totali non può essere inferiore alle prenotazioni già confermate");
        }

        return this.eventiRepository.save(found);
    }

    public void findByIdAndDelete(UUID id, Utente organizzatore) {
        Evento found = this.findById(id);

        if (!found.getCreatore().getId().equals(organizzatore.getId()))
            throw new UnauthorizedException("Non sei autorizzato a eliminare questo evento");

        if (found.getStato() == StatoEvento.CANCELLATO)
            throw new BadRequestException("Questo evento è già cancellato");

        this.eventiRepository.delete(found);
        log.info("Evento con id {} eliminato da {}", id, organizzatore.getUsername());
    }

    public Evento cancellaEvento(UUID id, Utente organizzatore) {
        Evento found = this.findById(id);

        if (!found.getCreatore().getId().equals(organizzatore.getId()))
            throw new UnauthorizedException("Non sei autorizzato a cancellare questo evento");

        if (found.getStato() == StatoEvento.CANCELLATO)
            throw new BadRequestException("Questo evento è già cancellato");

        found.setStato(StatoEvento.CANCELLATO);
        return this.eventiRepository.save(found);
    }
}
