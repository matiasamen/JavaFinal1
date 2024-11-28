package matias.coderhouse.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import matias.coderhouse.example.entity.Product;
import matias.coderhouse.example.model.Cliente;
import matias.coderhouse.example.model.Factura;
import matias.coderhouse.example.session.Session;
import matias.coderhouse.example.session.SessionService;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping
    @Operation(summary = "Crear una nueva sesión", description = "Crea una nueva sesión para un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sesión creada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<Session> createSession(@RequestBody @Parameter(description = "Información del usuario", required = true) SecurityProperties.User user) {
        Session session = sessionService.createSession(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @PostMapping("/{sessionId}/products")
    @Operation(summary = "Agregar producto a la sesión", description = "Agrega un producto específico a una sesión existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto agregado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    public ResponseEntity<Session> addProductToSession(@PathVariable @Parameter(description = "Identificador único de la sesión", required = true) Long sessionId, @RequestBody @Parameter(description = "Producto a agregar", required = true) Product product) {
        Session session = sessionService.addProductToSession(sessionId, product);
        return ResponseEntity.ok(session);
    }
    // **ACLARACIÓN: Acá integramos  RestTemplate directamente en el metodo checkoutSession.**
    @PostMapping("/{sessionId}/checkout")
    @Operation(summary = "Realiza el checkout de una sesión y genera una factura", description = "Genera una factura para una sesión específica y un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Factura.class))),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    public ResponseEntity<Factura> checkoutSession(@PathVariable @Parameter(description = "Identificador único de la sesión", required = true) Long sessionId, @RequestBody @Parameter(description = "Información del cliente", required = true) Cliente cliente) {
        Session session = sessionService.getSessionById(sessionId);
        Factura factura = sessionService.generateFactura(session, cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(factura);
    }
}

