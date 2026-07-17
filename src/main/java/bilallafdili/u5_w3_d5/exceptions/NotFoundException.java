package bilallafdili.u5_w3_d5.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super(id + " non è presente nel DB");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
