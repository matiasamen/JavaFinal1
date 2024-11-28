package matias.coderhouse.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import matias.coderhouse.example.model.Factura;

@Entity
@Data
@Schema (description = "Detalle de una factura, especificando el producto, cantidad y precio unitario")
public class DetalleFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    @Schema(description = "Factura a la que pertenece este detalle")
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @Schema(description = "Producto asociado a este detalle")
    private Product producto;

    @Schema(description = "Cantidad del producto en este detalle")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int cantidad;

    @Schema(description = "Precio unitario del producto en este detalle")
    @Min(value = 0, message = "El precio unitario debe ser mayor o igual a 0")
    private double precioUnitario;
}
