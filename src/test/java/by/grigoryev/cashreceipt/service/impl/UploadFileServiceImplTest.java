package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.service.UploadFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

class UploadFileServiceImplTest {

    private UploadFileService uploadFileService;

    public static final String CASH_RECEIPT = """
            Cash Receipt
            DATE: 2022-12-21 TIME: 15:23:48
            ----------------------------------------
            QTY    DESCRIPTION      PRICE    TOTAL
            6   | Woolen gloves   | 30.89  | 185.34
            6   | Golden samovar  | 100.99 | 605.94
            7   | Rock-drill Bosh | 575.25 | 4026.75
            ========================================
            TOTAL: 4818.03
            DiscountCard -10% : -481.803
            PromoDiscount -10% : "Woolen gloves"
            more than 5 items: -18.534
            PromoDiscount -10% : "Rock-drill Bosh"
            more than 5 items: -402.675
            TOTAL PAID: 3915.02
            """;

    @BeforeEach
    void setUp() {
        uploadFileService = spy(new UploadFileServiceImpl());
    }

    @Test
    @DisplayName("testing uploadFile method for the existence of the file")
    void uploadFile() throws IOException {
        Path absolutePath = Paths.get("CashReceipt.txt").toAbsolutePath();
        Path expectedValue = Files.write(absolutePath, CASH_RECEIPT.getBytes());

        Path uploadFile = uploadFileService.uploadFile(CASH_RECEIPT);

        assertTrue(Files.exists(expectedValue));
        assertThat(Files.readString(expectedValue)).isEqualTo(Files.readString(uploadFile));
    }

}