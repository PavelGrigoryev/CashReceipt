package ru.clevertec.cashreceipt.controller.xml;

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
import ru.clevertec.cashreceipt.dto.ProductDto;
import ru.clevertec.cashreceipt.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/xml/products", produces = "application/xml")
@Tag(name = "XmlProduct", description = "The Xml Product API")
public class XmlProductController {

    private final ProductService productService;

    @Operation(summary = "Find all products", tags = "XmlProduct")
    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Operation(
            summary = "Find product by {id}", tags = "XmlProduct",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "2")
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

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
                                       <total>5.76</total>
                                       <promotion>true</promotion>
                                     </ProductDto>
                                    """))
            )
    )
    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productDto));
    }

    @Operation(
            summary = "Update product quantity by id", tags = "XmlProduct",
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
            summary = "Delete product by {id}", tags = "XmlProduct",
            parameters = @Parameter(name = "id", description = "Enter product id here", example = "1")
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("The product with ID " + id + " has been deleted");
    }

}
