package ru.clevertec.cashreceipt.service.impl;

import ru.clevertec.cashreceipt.service.UploadFileService;
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UploadFileServiceImplTest {

    @Spy
    private UploadFileService uploadFileService = new UploadFileServiceImpl();

    private static final String CASH_RECEIPT = "CashReceipt";

    @Test
    @DisplayName("test uploadFile method should save a file.txt")
    void testUploadFileShouldSaveFile() throws IOException {
        FileSystem fileSystem = MemoryFileSystemBuilder.newEmpty().build();

        Path path = fileSystem.getPath("CashReceipt1.txt");
        Path expectedValue = Files.write(path, CASH_RECEIPT.getBytes());

        Path actualValue = uploadFileService.uploadFile(CASH_RECEIPT);

        assertThat(Files.exists(expectedValue)).isTrue();
        assertThat(Files.readString(expectedValue)).isEqualTo(Files.readString(actualValue));

        fileSystem.close();
    }

}