package matias.coderhouse.example.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import matias.coderhouse.example.model.Factura;

@Schema(description = "Repositorio para gestionar las facturas")
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
