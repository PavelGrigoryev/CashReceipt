package ru.clevertec.cashreceipt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.cashreceipt.controller.swagger.SwaggerDiscountCard;
import ru.clevertec.cashreceipt.dto.DiscountCardDto;
import ru.clevertec.cashreceipt.service.DiscountCardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discountCards")
public class DiscountCardController implements SwaggerDiscountCard {

    private final DiscountCardService discountCardService;

    @GetMapping
    @Override
    public ResponseEntity<List<DiscountCardDto>> findAll() {
        return ResponseEntity.ok(discountCardService.findAll());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<DiscountCardDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(discountCardService.findById(id));
    }

    @PostMapping
    @Override
    public ResponseEntity<DiscountCardDto> save(@RequestBody DiscountCardDto discountCardDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(discountCardService.save(discountCardDto));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        discountCardService.deleteById(id);
        return ResponseEntity.ok("The discount card with ID " + id + " has been deleted");
    }

}
