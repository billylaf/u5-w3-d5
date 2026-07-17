package bilallafdili.u5_w3_d5.entities;

import enums.StatoEvento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "eventi")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Evento {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String titolo;

    @Column(length = 1000)
    private String descrizione;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private String luogo;

    @Column(nullable = false)
    private Integer postiDisponibili;

    @Column(nullable = false)
    private Integer postiTotali;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoEvento stato;

    @ManyToOne
    @JoinColumn(name = "creatore_id", nullable = false)
    private Utente creatore;

    public Evento(String titolo, String descrizione, LocalDateTime data, String luogo,
                  Integer postiTotali, Utente creatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.postiTotali = postiTotali;
        this.postiDisponibili = postiTotali;
        this.stato = StatoEvento.PROGRAMMATO;
        this.creatore = creatore;
    }
}
