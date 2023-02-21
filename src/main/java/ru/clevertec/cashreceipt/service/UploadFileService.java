package ru.clevertec.cashreceipt.service;

import java.nio.file.Path;

public interface UploadFileService {

    Path uploadFile(String cashReceipt);

}
