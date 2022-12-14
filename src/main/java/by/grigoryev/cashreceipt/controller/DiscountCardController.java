package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discountCard")
public class DiscountCardController {

    private final DiscountCardService discountCardService;

    @GetMapping
    public ResponseEntity<List<DiscountCard>> findAll() {
        return ResponseEntity.ok(discountCardService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountCard> findById(@PathVariable Long id) {
        return ResponseEntity.ok(discountCardService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DiscountCard> save(@RequestBody DiscountCard discountCard) {
        return ResponseEntity.ok(discountCardService.save(discountCard));
    }

}
