package ru.clevertec.cashreceipt.service.factory.impl;

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
class TxtUploadFileFactoryTest {

    @Spy
    private TxtUploadFileFactory txtUploadFileFactory;

    @Test
    @DisplayName("test uploadFileTxt method should save a file.txt")
    void testUploadFileTxtShouldSaveFileTxt() throws IOException {
        String cashReceipt = "Test cash receipt";
        FileSystem fileSystem = MemoryFileSystemBuilder.newEmpty().build();

        Path path = fileSystem.getPath("CashReceipt1.txt");
        Path expectedValue = Files.write(path, cashReceipt.getBytes());

        Path actualValue = txtUploadFileFactory.uploadFile(cashReceipt);

        assertThat(Files.exists(expectedValue)).isTrue();
        assertThat(Files.readString(expectedValue)).isEqualTo(Files.readString(actualValue));

        fileSystem.close();
    }

}
