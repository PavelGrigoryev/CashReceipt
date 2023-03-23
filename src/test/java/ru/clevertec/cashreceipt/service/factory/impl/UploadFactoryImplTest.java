package ru.clevertec.cashreceipt.service.factory.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cashreceipt.service.factory.UploadFactory;
import ru.clevertec.cashreceipt.service.factory.UploadFileFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UploadFactoryImplTest {

    @Spy
    private UploadFactory uploadFactory;

    @BeforeEach
    void setUp() {
        uploadFactory = new UploadFactoryImpl();
    }

    @Test
    @DisplayName("test UploadFactoryImpl should return TxtUploadFileFactory")
    void testUploadFactoryShouldReturnTxtUploadFileFactory() {
        UploadFileFactory uploadFileFactory = uploadFactory.create("txt");
        assertThat(uploadFileFactory).isInstanceOf(TxtUploadFileFactory.class);
    }

    @Test
    @DisplayName("test UploadFactoryImpl should return PdfUploadFileFactory")
    void testUploadFactoryShouldReturnPdfUploadFileFactory() {
        UploadFileFactory uploadFileFactory = uploadFactory.create("pdf");
        assertThat(uploadFileFactory).isInstanceOf(PdfUploadFileFactory.class);
    }

    @Test
    @DisplayName("test should throw IllegalArgumentException if the create method accepts neither txt or pdf")
    void testShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> uploadFactory.create("doc"));
    }

}
