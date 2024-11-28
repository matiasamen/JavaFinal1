package matias.coderhouse.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Schema(description = "Línea de una factura, representando un producto y su cantidad")
@Data
public class LineaFactura {
    @Schema(description = "ID del producto")
    private Long productoId;

    @Schema(description = "Cantidad del producto")
    private Integer cantidad;

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Double precioUnitario;
}
