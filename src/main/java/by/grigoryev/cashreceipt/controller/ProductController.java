package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "Product", description = "The Product API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Find all products", tags = "Product")
    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Operation(
            summary = "Find product by {id}", tags = "Product",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "2")
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(
            summary = "Save new product", tags = "Product",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for product",
                    content = @Content(schema = @Schema(implementation = ProductDto.class)
                            , examples = @ExampleObject("""
                            {
                              "quantity": 1,
                              "name": "Spicy pretzel",
                              "price": "5.76",
                              "promotion": true
                            }
                            """))
            )
    )
    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productDto));
    }

    @Operation(
            summary = "Update product quantity by id", tags = "Product",
            parameters = {
                    @Parameter(name = "id", description = "Enter product id here",
                            example = "3"),
                    @Parameter(name = "quantity", description = "Enter product quantity here",
                            example = "9")
            }
    )
    @PutMapping
    public ResponseEntity<ProductDto> update(@RequestParam Long id, Integer quantity) {
        return ResponseEntity.ok(productService.update(id, quantity));
    }

    @Operation(
            summary = "Delete product by {id}", tags = "Product",
            parameters = @Parameter(name = "id", description = "Enter product id here", example = "1")
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("The product with ID " + id + " has been deleted");
    }

}
