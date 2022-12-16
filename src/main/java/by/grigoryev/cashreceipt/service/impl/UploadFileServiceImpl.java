package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Override
    public void uploadFile(String cashReceipt) {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "CashReceipt.txt";

        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            outputStream.write(cashReceipt.getBytes());
            log.info("uploadFile {}", fileLocation);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
