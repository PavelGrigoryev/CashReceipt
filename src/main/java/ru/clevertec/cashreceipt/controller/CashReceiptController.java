package ru.clevertec.cashreceipt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.cashreceipt.controller.swagger.SwaggerCashReceipt;
import ru.clevertec.cashreceipt.service.CashReceiptLogicService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cashReceipts")
public class CashReceiptController implements SwaggerCashReceipt {

    private final CashReceiptLogicService cashReceiptLogicService;

    @GetMapping
    @Override
    public ResponseEntity<String> createCashReceipt(@RequestParam String idAndQuantity, String discountCardNumber) {
        return ResponseEntity.ok(cashReceiptLogicService.createCashReceipt(idAndQuantity, discountCardNumber));
    }

}
