package bilallafdili.u5_w3_d5.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private List<String> errorsList;

    public ValidationException(List<String> errorsList) {
        super("errori di validazione");
        this.errorsList = errorsList;
    }
}
