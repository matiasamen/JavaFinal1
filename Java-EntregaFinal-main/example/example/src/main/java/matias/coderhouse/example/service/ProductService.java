package matias.coderhouse.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import matias.coderhouse.example.entity.Product;
import matias.coderhouse.example.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@Service
@Schema(description = "Servicio para gestionar operaciones relacionadas con productos")
public class ProductService {
    @Autowired
    private ProductoRepository productoRepository;

    @Operation(summary = "Obtener todos los productos", description = "Recupera una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos recuperados correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    })
    public List<Product> findAll() {
        return productoRepository.findAll();
    }

    @Operation(summary = "Buscar producto por ID", description = "Recupera un producto específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public Optional<Product> findById (@Parameter(description = "Identificador único del producto", example = "1") Long id) {
        return productoRepository.findById(id);
    }

    @Operation(summary = "Calcular el total de productos", description = "Calcula el monto total de una lista de productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total calculado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Double.class)))
    })
    public Double calcularTotal(@Parameter(description = "Lista de productos para calcular el total", required = true) List<Product> productos) {
        double total = 0;
        for (Product producto : productos) {
            total += producto.getPrice() * producto.getCantidad();
        }
        return total;
    }

    @Operation(summary = "Guardar un producto", description = "Guarda un nuevo producto en el repositorio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto guardado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    })
    public Product save(@Parameter(description = "Producto a guardar", required = true) Product product) {
        return productoRepository.save(product);
    }
}









