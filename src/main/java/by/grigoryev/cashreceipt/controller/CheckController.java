package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
public class CheckController {

    private final CheckService checkService;

    @GetMapping
    public ResponseEntity<String> createCheck(@RequestParam String idAndQuantity, String discountCardNumber) {
        return ResponseEntity.ok(checkService.createCheck(idAndQuantity, discountCardNumber));
    }

}
