package bilallafdili.u5_w3_d5.repositories;

import bilallafdili.u5_w3_d5.entities.Evento;
import bilallafdili.u5_w3_d5.entities.Utente;
import enums.StatoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventiRepository extends JpaRepository<Evento, UUID> {
    List<Evento> findByCreatore(Utente creatore);

    List<Evento> findByPostiDisponibiliGreaterThan0AndStato(StatoEvento stato);

    List<Evento> findByCreatoreAndStatoNot(Utente creatore, StatoEvento stato);
}
