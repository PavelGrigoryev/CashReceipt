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

@Tag(name = "XmlDiscountCard", description = "The Xml DiscountCard API")
public interface XmlSwaggerDiscountCard {

    @Operation(summary = "Find all discount cards", tags = "XmlDiscountCard")
    ResponseEntity<List<DiscountCardDto>> findAll();

    @Operation(summary = "Find discount card by {id}", tags = "XmlDiscountCard",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "3"))
    ResponseEntity<DiscountCardDto> findById(Long id);

    @Operation(
            summary = "Save new discount card", tags = "XmlDiscountCard",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for discount card",
                    content = @Content(schema = @Schema(implementation = DiscountCardDto.class),
                            mediaType = "application/xml",
                            examples = @ExampleObject("""
                                            <DiscountCardDto>
                                              <discountCardNumber>7878</discountCardNumber>
                                              <discountPercentage>7.5</discountPercentage>
                                            </DiscountCardDto>
                                    """))
            )
    )
    ResponseEntity<DiscountCardDto> save(DiscountCardDto discountCardDto);

    @Operation(summary = "Delete discount card by {id}", tags = "XmlDiscountCard",
            parameters = @Parameter(name = "id", description = "Enter discount card id here", example = "1"))
    ResponseEntity<String> deleteById(Long id);

}
