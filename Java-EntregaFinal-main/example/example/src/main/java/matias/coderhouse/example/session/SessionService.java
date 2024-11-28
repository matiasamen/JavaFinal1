package matias.coderhouse.example.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import matias.coderhouse.example.entity.DetalleFactura;
import matias.coderhouse.example.entity.Product;
import matias.coderhouse.example.exception.NotFoundException;
import matias.coderhouse.example.model.Cliente;
import matias.coderhouse.example.model.Factura;
import matias.coderhouse.example.repository.SessionRepository;
import matias.coderhouse.example.repository.FacturaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
@Schema(description = "Servicio para gestionar operaciones relacionadas con sesiones")
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;
    private FacturaRepository facturaRepository;

    @Operation(summary = "Crear una nueva sesión", description = "Crea una nueva sesión para un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sesión creada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public Session createSession(@Parameter(description = "Información del usuario", required = true) SecurityProperties.User user) {
        Session session = new Session();
        session.setUser(user);
        session.setCreatedAt(new Date());
        session.setStatus(Session.STATUS_OPEN);
        return sessionRepository.save(session);
    }

    @Operation(summary = "Obtener sesión por ID", description = "Recupera una sesión específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesión encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    public Session getSessionById(@Parameter(description = "Identificador único de la sesión", example = "1") Long sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }

    @Operation(summary = "Agregar producto a la sesión", description = "Agrega un producto específico a una sesión existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto agregado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    public Session addProductToSession(@Parameter(description = "Identificador único de la sesión", example = "1") Long sessionId, @Parameter(description = "Producto a agregar", required = true) Product product) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Sesión no encontrada"));
        session.getProducts().add(product);
        return sessionRepository.save(session);
    }

    @Operation(summary = "Calcular el total de productos en la sesión", description = "Calcula el monto total de los productos en una sesión específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total calculado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Double.class)))
    })
    public Double calculateTotal(@Parameter(description = "Sesión para calcular el total", required = true) Session session) {
        return session.getProducts().stream()
                .mapToDouble(product -> product.getPrice() * product.getCantidad())
                .sum();
    }

    @Operation(summary = "Generar factura para una sesión", description = "Genera una factura para una sesión específica y un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Factura.class))),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    public Factura generateFactura(@Parameter(description = "Sesión para generar la factura", required = true) Session session, @Parameter(description = "Información del cliente", required = true) Cliente cliente) {
        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setFecha(new Date());

        Double total = calculateTotal(session);
        factura.setTotal(total);

        List<DetalleFactura> detallesFactura = new ArrayList<>();
        for (Product producto : session.getProducts()) {
            DetalleFactura detalle = new DetalleFactura();
            detalle.setProducto(producto);
            detalle.setCantidad(producto.getCantidad());
            detalle.setPrecioUnitario(producto.getPrice());
            detalle.setFactura(factura);
            detallesFactura.add(detalle);
        }
        factura.setDetallesFactura(detallesFactura);

        facturaRepository.save(factura);
        closeSession(session.getId());

        return factura;
    }

    @Operation(summary = "Cerrar sesión", description = "Cierra una sesión específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    public void closeSession(@Parameter(description = "Identificador único de la sesión", example = "1") Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Sesión no encontrada"));
        session.setStatus(Session.STATUS_CLOSED);
        sessionRepository.save(session);
    }
}

