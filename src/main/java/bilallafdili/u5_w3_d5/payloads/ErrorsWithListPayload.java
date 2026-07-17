package bilallafdili.u5_w3_d5.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorsWithListPayload {
    private String message;
    private LocalDateTime timestamp;
    private List<String> errorsList;
}