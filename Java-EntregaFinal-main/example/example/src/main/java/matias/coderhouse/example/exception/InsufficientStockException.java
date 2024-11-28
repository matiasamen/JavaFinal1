package matias.coderhouse.example.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Excepción lanzada cuando no hay suficiente stock para un producto")
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
