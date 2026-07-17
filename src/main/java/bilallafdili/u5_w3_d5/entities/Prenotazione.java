package bilallafdili.u5_w3_d5.entities;


import enums.StatoPrenotazione;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Prenotazione {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @Column(name = "data_prenotazione", nullable = false)
    private LocalDateTime dataPrenotazione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoPrenotazione stato;

    public Prenotazione(Evento evento, Utente utente) {
        this.evento = evento;
        this.utente = utente;
        this.dataPrenotazione = LocalDateTime.now();
        this.stato = StatoPrenotazione.CONFERMATA;
    }
}
