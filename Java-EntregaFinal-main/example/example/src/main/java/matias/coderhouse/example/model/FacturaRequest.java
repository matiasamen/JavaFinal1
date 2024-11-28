package matias.coderhouse.example.model;

import jakarta.validation.constraints.Min;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data
@Schema ( description = "Solicitud para crear una nueva factura")
public class FacturaRequest {

    @Schema( description = "Información del cliente", required = true)
    private Cliente cliente;

    @Schema (description = "Lista de ítems de la factura", required = true )
    private List<LineaFactura> lineas;

    @Schema(description = "Cantidad de productos a incluir en la factura")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}
