package matias.coderhouse.example.session;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import matias.coderhouse.example.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Schema(description = "Entidad que representa una sesión de usuario en el sistema")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la sesión", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "Usuario asociado a la sesión", implementation = SecurityProperties.User.class)
    private SecurityProperties.User user;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @ArraySchema(schema = @Schema(description = "Lista de productos asociados a la sesión", implementation = Product.class))
    private List<Product> products = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha y hora de creación de la sesión", example = "2024-11-26T14:02:00")
    private Date createdAt;

    @Schema(description = "Estado de la sesión", allowableValues = {"OPEN", "CLOSED"}, example = "OPEN")
    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_CLOSED = "CLOSED";
    private String status;
}

