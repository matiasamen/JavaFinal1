package matias.coderhouse.example.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import matias.coderhouse.example.exception.InsufficientStockException;
import matias.coderhouse.example.model.Factura;
import matias.coderhouse.example.entity.Product;
import matias.coderhouse.example.model.FacturaRequest;
import matias.coderhouse.example.model.FacturaService;
import matias.coderhouse.example.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;


import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productoService;

    @Autowired
    private FacturaService facturaService;

    @PostMapping ("/facturas")
    public ResponseEntity<Factura> crearFactura (@RequestBody FacturaRequest facturaRequest) {
        Factura factura = new Factura();
        factura = facturaService.crearNuevaFactura(factura, facturaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(factura);
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam @Parameter (description = "Nombre del producto")
    @Schema (type = "string", example = "PRODUCTO-123") String name) {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/productos/{id}")
    @Operation (summary = "Buscar producto por ID", description = "Devuelve un producto si se encuentra por el ID especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class ),
            examples = {
                @ExampleObject(value =  "{\"id\": 1, \"name\": \"PRODUCTO-123\", \"price\": 17.98}")
            })),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Product> getProductById(@PathVariable @Parameter (description = "Identificador único del producto" ) Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException
            ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(
            @RequestBody @Parameter (description = "Datos del nuevo producto" )
            @Schema (description = "Información detallada del producto a crear") Product product) {
        Product newProduct = productoService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }
}


