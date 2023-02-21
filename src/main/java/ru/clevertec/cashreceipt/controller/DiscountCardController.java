package ru.clevertec.cashreceipt.controller;

import ru.clevertec.cashreceipt.dto.DiscountCardDto;
import ru.clevertec.cashreceipt.service.DiscountCardService;
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
@RequestMapping("/discountCards")
@Tag(name = "DiscountCard", description = "The DiscountCard API")
public class DiscountCardController {

    private final DiscountCardService discountCardService;

    @Operation(summary = "Find all discount cards", tags = "DiscountCard")
    @GetMapping
    public ResponseEntity<List<DiscountCardDto>> findAll() {
        return ResponseEntity.ok(discountCardService.findAll());
    }

    @Operation(
            summary = "Find discount card by {id}", tags = "DiscountCard",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "3")
    )
    @GetMapping("/{id}")
    public ResponseEntity<DiscountCardDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(discountCardService.findById(id));
    }

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
    @PostMapping
    public ResponseEntity<DiscountCardDto> save(@RequestBody DiscountCardDto discountCardDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(discountCardService.save(discountCardDto));
    }

    @Operation(
            summary = "Delete discount card by {id}", tags = "DiscountCard",
            parameters = @Parameter(name = "id", description = "Enter discount card id here", example = "1")
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        discountCardService.deleteById(id);
        return ResponseEntity.ok("The discount card with ID " + id + " has been deleted");
    }

}
