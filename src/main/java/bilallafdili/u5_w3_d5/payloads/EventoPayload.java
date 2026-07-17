package bilallafdili.u5_w3_d5.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class EventoPayload {
    @NotBlank(message = "Il titolo è obbligatorio")
    private String titolo;

    private String descrizione;

    @NotNull(message = "La data è obbligatoria")
    @Future(message = "La data deve essere nel futuro")
    private LocalDateTime data;

    @NotBlank(message = "Il luogo è obbligatorio")
    private String luogo;

    @NotNull(message = "Il numero di posti totali è obbligatorio")
    @Min(value = 1, message = "Il numero di posti deve essere almeno 1")
    private Integer postiTotali;
}
