package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.service.CashReceiptLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cashReceipts")
public class CashReceiptController {

    private final CashReceiptLogicService cashReceiptLogicService;

    @GetMapping
    public ResponseEntity<String> createCashReceipt(@RequestParam String idAndQuantity, String discountCardNumber) {
        return ResponseEntity.ok(cashReceiptLogicService.createCashReceipt(idAndQuantity, discountCardNumber));
    }

}
