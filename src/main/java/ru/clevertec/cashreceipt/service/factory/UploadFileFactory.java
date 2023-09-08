package ru.clevertec.cashreceipt.service.factory;

import java.nio.file.Path;

/**
 * The UploadFileFactory interface represents a factory that can upload files in various formats
 */
public interface UploadFileFactory {

    /**
     * Uploads a file in various formats using the given data string and returns the path to the uploaded file
     *
     * @param cashReceipt a string containing information to be inserted into the uploaded file
     * @return the path to the uploaded file
     */
    Path uploadFile(String cashReceipt);

}
