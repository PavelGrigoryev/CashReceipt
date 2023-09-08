package ru.clevertec.cashreceipt.service.factory.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class PdfUploadFileFactoryTest {

    @Spy
    private PdfUploadFileFactory pdfUploadFileFactory;

    @Test
    @DisplayName("test uploadFilePdf method should save a file.pdf")
    void testUploadFilePdfShouldSaveFilePdf() throws IOException {
        String cashReceipt = "Test cash receipt";
        Path pdfPath = pdfUploadFileFactory.uploadFile(cashReceipt);

        assertThat(Files.exists(pdfPath)).isTrue();
        assertThat(Files.size(pdfPath)).isGreaterThan(5);
    }

}
