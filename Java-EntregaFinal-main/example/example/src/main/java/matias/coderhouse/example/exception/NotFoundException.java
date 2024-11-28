package matias.coderhouse.example.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Excepción lanzada cuando un recurso no se encuentra")
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
