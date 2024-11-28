package matias.coderhouse.example.model;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import matias.coderhouse.example.entity.DetalleFactura;
import matias.coderhouse.example.entity.Product;
import matias.coderhouse.example.exception.InsufficientStockException;
import matias.coderhouse.example.exception.NotFoundException;
import matias.coderhouse.example.repository.FacturaRepository;
import matias.coderhouse.example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service

public class FacturaService {
    @Autowired
    private ProductService productoService;
    @Autowired
    private FacturaRepository facturaRepository;
    private static final Logger log = LoggerFactory.getLogger(FacturaService.class);

    @Operation(summary = "Crear una nueva factura", description = "Genera una nueva factura basada en la solicitud proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Factura.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InsufficientStockException.class)))
    })

    public Factura crearNuevaFactura(
            @RequestBody(required = true) Factura factura,
            @RequestBody(required = true) FacturaRequest facturaRequest) {
        double total = 0;

        List<DetalleFactura> detallesFactura = new ArrayList<>();
        for (LineaFactura linea : facturaRequest.getLineas()) {
            Product producto = productoService.findById(linea.getProductoId()).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
            if (producto.getCantidad() < linea.getCantidad()) {
                throw new InsufficientStockException("Stock insuficiente para el producto " + producto.getId());
            }
            DetalleFactura detalle = new DetalleFactura();
            detalle.setProducto(producto);
            detalle.setCantidad(linea.getCantidad());
            detalle.setPrecioUnitario(linea.getPrecioUnitario());
            detalle.setFactura(factura);
            detallesFactura.add(detalle);
            total += linea.getCantidad() * linea.getPrecioUnitario();
        }

        factura.setDetallesFactura(detallesFactura);
        factura.setTotal(total);
        factura.setFecha(new Date());
        return facturaRepository.save(factura);
    }
}
