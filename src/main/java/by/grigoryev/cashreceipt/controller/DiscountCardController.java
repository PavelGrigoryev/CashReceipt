package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.service.DiscountCardService;
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
    public ResponseEntity<List<DiscountCard>> findAll() {
        return ResponseEntity.ok(discountCardService.findAll());
    }

    @Operation(
            summary = "Find discount card by {id}", tags = "DiscountCard",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "3")
    )
    @GetMapping("/{id}")
    public ResponseEntity<DiscountCard> findById(@PathVariable Long id) {
        return ResponseEntity.ok(discountCardService.findById(id));
    }

    @Operation(
            summary = "Save new discount card", tags = "DiscountCard",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for discount card",
                    content = @Content(schema = @Schema(implementation = DiscountCard.class)
                            , examples = @ExampleObject("""
                            {
                              "discountCardNumber": "7878",
                              "discountPercentage": 7.5
                            }
                            """))
            )
    )
    @PostMapping
    public ResponseEntity<DiscountCard> save(@RequestBody DiscountCard discountCard) {
        return ResponseEntity.status(HttpStatus.CREATED).body(discountCardService.save(discountCard));
    }

}
