package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.service.CheckLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checks")
public class CheckController {

    private final CheckLogicService checkLogicService;

    @GetMapping
    public ResponseEntity<String> createCheck(@RequestParam String idAndQuantity, String discountCardNumber) {
        return ResponseEntity.ok(checkLogicService.createCheck(idAndQuantity, discountCardNumber));
    }

}
