package ru.clevertec.cashreceipt.service.factory;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The TxtUploadFileFactory class represents a factory for creating objects that can upload TXT files
 */
@Slf4j
public class TxtUploadFileFactory implements UploadFileFactory {

    /**
     * Uploads a file in TXT format using the given cashReceipt string and returns the path to the uploaded file
     *
     * @param cashReceipt a string containing information to be inserted into the uploaded TXT file
     * @return the path to the uploaded file
     */
    @Override
    public Path uploadFile(String cashReceipt) {
        Path path = Paths.get("src/main/resources/txt/CashReceipt.txt");
        try {
            Path file = Files.write(path, cashReceipt.getBytes());
            log.info("uploadFileTxt {}", file);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return path;
    }

}
