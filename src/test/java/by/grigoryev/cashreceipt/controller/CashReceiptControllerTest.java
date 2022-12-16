package by.grigoryev.cashreceipt.controller;

import by.grigoryev.cashreceipt.service.CashReceiptLogicService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CashReceiptController.class)
class CashReceiptControllerTest {

    public static final String ID_AND_QUANTITY = "3-6 2-6 1-7";
    public static final String DISCOUNT_CARD_NUMBER = "1234";
    public static final String STRING_CONTENT = """
            Cash Receipt
            DATE: 2022-12-15 TIME: 22: 52: 30
            ----------------------------------------
            QTY    DESCRIPTION      PRICE    TOTAL
            6  | Перчатки шерсть | 30.89  | 185.34
            6  | Самовар золотой | 100.99 | 605.94
            7  | Перфоратор Bosh | 575.25 | 4026.75
            ========================================
            TOTAL: 4818.03
            DiscountCard -10%: -481.803
            PromoDiscount -10%: "Перчатки шерсть"
            more then 5 items: -18.534
            PromoDiscount -10% : "Перфоратор Bosh"
            more then 5 items: -402.675
            TOTAL PAID: 3915.02
            """;

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private CashReceiptLogicService cashReceiptLogicService;

    @Test
    @DisplayName("testing createCashReceipt endpoint")
    void createCashReceipt() throws Exception {
        doReturn(STRING_CONTENT).when(cashReceiptLogicService)
                .createCashReceipt(ID_AND_QUANTITY, DISCOUNT_CARD_NUMBER);

        mockMvc.perform(get("/cashReceipts/?idAndQuantity=" + ID_AND_QUANTITY
                            + "&discountCardNumber=" + DISCOUNT_CARD_NUMBER))
                .andExpect(status().isOk())
                .andExpect(content().string(STRING_CONTENT));
    }

}