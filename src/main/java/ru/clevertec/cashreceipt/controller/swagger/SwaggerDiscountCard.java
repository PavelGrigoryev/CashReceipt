package ru.clevertec.cashreceipt.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.clevertec.cashreceipt.dto.DiscountCardDto;

import java.util.List;

@Tag(name = "DiscountCard", description = "The DiscountCard API")
public interface SwaggerDiscountCard {

    @Operation(summary = "Find all discount cards", tags = "DiscountCard")
    ResponseEntity<List<DiscountCardDto>> findAll();

    @Operation(summary = "Find discount card by {id}", tags = "DiscountCard",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "3"))
    ResponseEntity<DiscountCardDto> findById(Long id);

    @Operation(
            summary = "Save new discount card", tags = "DiscountCard",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for discount card",
                    content = @Content(schema = @Schema(implementation = DiscountCardDto.class)
                            , examples = @ExampleObject("""
                            {
                              "discountCardNumber": "7878",
                              "discountPercentage": 7.5
                            }
                            """))
            )
    )
    ResponseEntity<DiscountCardDto> save(DiscountCardDto discountCardDto);

    @Operation(summary = "Delete discount card by {id}", tags = "DiscountCard",
            parameters = @Parameter(name = "id", description = "Enter discount card id here", example = "1"))
    ResponseEntity<String> deleteById(Long id);

}
