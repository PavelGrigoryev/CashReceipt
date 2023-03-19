package ru.clevertec.cashreceipt.service;

import java.nio.file.Path;

public interface UploadFileService {

    Path uploadFileTxt(String cashReceipt);

    Path uploadFilePdf(String cashReceipt);

}
