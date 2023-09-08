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

@Tag(name = "XmlProduct", description = "The Xml Product API")
public interface XmlSwaggerProduct {

    @Operation(summary = "Find all products", tags = "XmlProduct")
    ResponseEntity<List<ProductDto>> findAll();

    @Operation(summary = "Find product by {id}", tags = "XmlProduct",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "2"))
    ResponseEntity<ProductDto> findById(Long id);

    @Operation(
            summary = "Save new product", tags = "XmlProduct",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "RequestBody for product",
                    content = @Content(schema = @Schema(implementation = ProductDto.class),
                            mediaType = "application/xml",
                            examples = @ExampleObject("""
                                    <ProductDto>
                                       <quantity>1</quantity>
                                       <name>Spicy pretzel</name>
                                       <price>5.76</price>
                                       <promotion>true</promotion>
                                     </ProductDto>
                                    """))
            )
    )
    ResponseEntity<ProductDto> save(ProductDto productDto);

    @Operation(
            summary = "Update product quantity by id", tags = "XmlProduct",
            parameters = {
                    @Parameter(name = "id", description = "Enter product id here",
                            example = "3"),
                    @Parameter(name = "quantity", description = "Enter product quantity here",
                            example = "9")
            }
    )
    ResponseEntity<ProductDto> update(Long id, Integer quantity);

    @Operation(summary = "Delete product by {id}", tags = "XmlProduct",
            parameters = @Parameter(name = "id", description = "Enter product id here", example = "1"))
    ResponseEntity<String> deleteById(Long id);

}
