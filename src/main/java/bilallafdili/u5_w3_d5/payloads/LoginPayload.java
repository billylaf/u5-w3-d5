package bilallafdili.u5_w3_d5.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginPayload(
        @NotBlank(message = "email è obbligatoria")
        @Email(message = "email deve essere in un formato valido")
        String email,

        @NotBlank(message = "la password è obbligatoria")
        String password
) {
}