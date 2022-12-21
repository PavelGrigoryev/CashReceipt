package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Override
    public Path uploadFile(String cashReceipt) {
        Path absolutePath = Paths.get("CashReceipt.txt").toAbsolutePath();
        try {
            Path file = Files.write(absolutePath, cashReceipt.getBytes());
            log.info("uploadFile {}", file);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return absolutePath;
    }

}
