package bilallafdili.u5_w3_d5.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
public class PrenotazionePayload {
    @NotNull(message = "L'ID dell'evento è obbligatorio")
    private UUID eventoId;
}