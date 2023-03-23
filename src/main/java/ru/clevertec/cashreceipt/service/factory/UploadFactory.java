package ru.clevertec.cashreceipt.service.factory;

/**
 * The UploadFactory interface represents a factory for creating objects that can upload files in various formats
 */
public interface UploadFactory {

    /**
     * Creates a factory object for uploading files in the specified format
     *
     * @param fileType string representing the file format
     * @return factory object for uploading files in the specified format
     */
    UploadFileFactory create(String fileType);

}
