package ru.clevertec.cashreceipt.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.clevertec.cashreceipt.dto.ProductDto;

import java.util.List;

@Tag(name = "Product", description = "The Product API")
public interface SwaggerProduct {

    @Operation(summary = "Find all products", tags = "Product")
    ResponseEntity<List<ProductDto>> findAll();

    @Operation(summary = "Find product by {id}", tags = "Product",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "2"))
    ResponseEntity<ProductDto> findById(Long id);

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
    ResponseEntity<ProductDto> save(ProductDto productDto);

    @Operation(
            summary = "Update product quantity by id", tags = "Product",
            parameters = {
                    @Parameter(name = "id", description = "Enter product id here",
                            example = "3"),
                    @Parameter(name = "quantity", description = "Enter product quantity here",
                            example = "9")
            }
    )
    ResponseEntity<ProductDto> update(Long id, Integer quantity);

    @Operation(
            summary = "Delete product by {id}", tags = "Product",
            parameters = @Parameter(name = "id", description = "Enter product id here", example = "1")
    )
    ResponseEntity<String> deleteById(Long id);

}
