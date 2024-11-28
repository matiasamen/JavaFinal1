package matias.coderhouse.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import matias.coderhouse.example.entity.DetalleFactura;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Schema(description = "Representa una factura de venta")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @Schema(description = "Cliente asociado a la factura")
    private Cliente cliente;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    @Schema(description = "Detalles de la factura (productos, cantidades, precios)")
    private List<DetalleFactura> detallesFactura;

    @Schema(description = "Fecha de emisión de la factura")
    private Date fecha;

    @Schema(description = "Total de la factura")
    private Double total;

    public int getCantidadTotalProductos() {
        int totalProductos = 0;
        for (DetalleFactura detalle : detallesFactura) {
            totalProductos += detalle.getCantidad();
        }
        return totalProductos;
    }
}

