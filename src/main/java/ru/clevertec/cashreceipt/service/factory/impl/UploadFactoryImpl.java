package ru.clevertec.cashreceipt.service.factory.impl;

import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.service.factory.UploadFactory;
import ru.clevertec.cashreceipt.service.factory.UploadFileFactory;

/**
 * The UploadFactoryImpl class represents a factory for creating objects that can upload files in various formats
 */
@Service
public class UploadFactoryImpl implements UploadFactory {

    /**
     * Creates a factory object for uploading files in the specified format
     *
     * @param fileType string representing the file format ("txt" or "pdf")
     * @return factory object for uploading files in the specified format
     * @throws IllegalArgumentException if the file type is not supported
     */
    @Override
    public UploadFileFactory create(String fileType) {
        if (fileType.equalsIgnoreCase("txt")) {
            return new TxtUploadFileFactory();
        } else if (fileType.equalsIgnoreCase("pdf")) {
            return new PdfUploadFileFactory();
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

}
