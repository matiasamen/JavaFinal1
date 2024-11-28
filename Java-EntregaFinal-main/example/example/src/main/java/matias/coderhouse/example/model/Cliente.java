package matias.coderhouse.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Información del cliente")
@Data
public class Cliente {
    @Schema(description = "Identificador único del cliente")
    private Long id;
    @Schema(description = "Nombre del cliente")
    private String nombre;
    @Schema(description = "Apellido del cliente")
    private String apellido;
}
