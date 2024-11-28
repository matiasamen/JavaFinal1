package matias.coderhouse.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import matias.coderhouse.example.session.Session;

import java.util.List;

@Data
@Entity
@Table(name = "products")
@Schema(description = "Representa un producto")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del producto")
    private String name;

    @Schema(description = "Precio del producto")
    private double price;

    @Schema(description = "Cantidad disponible del producto")
    private int cantidad;

    @ManyToMany(mappedBy = "products")
    @Schema(description = "Sesiones asociadas al producto")
    private List<Session> sessions;
}
