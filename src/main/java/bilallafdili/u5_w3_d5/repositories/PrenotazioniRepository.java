package bilallafdili.u5_w3_d5.repositories;

import bilallafdili.u5_w3_d5.entities.Evento;
import bilallafdili.u5_w3_d5.entities.Prenotazione;
import bilallafdili.u5_w3_d5.entities.Utente;
import enums.StatoPrenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByUtenteAndStato(Utente utente, StatoPrenotazione stato);

    boolean existsByUtenteAndEventoAndStato(Utente utente, Evento evento, StatoPrenotazione stato);

    List<Prenotazione> findByEvento(Evento evento);
}