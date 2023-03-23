package ru.clevertec.cashreceipt.service.factory.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.cashreceipt.service.factory.UploadFileFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The PdfUploadFileFactory class represents a factory for creating objects that can upload PDF files
 */
@Slf4j
public class PdfUploadFileFactory implements UploadFileFactory {

    /**
     * Uploads a file in PDF format using the given cashReceipt string and returns the path to the uploaded file
     *
     * @param cashReceipt a string containing information to be inserted into the uploaded PDF file
     * @return the path to the uploaded file
     */
    @Override
    public Path uploadFile(String cashReceipt) {
        Path path = Paths.get("src/main/resources/pdf/CashReceipt.pdf");
        try {
            Document document = new Document(PageSize.A4);

            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(path));
            document.open();

            PdfReader reader = new PdfReader("src/main/resources/pdf/Clevertec_Template.pdf");
            PdfImportedPage page = writer.getImportedPage(reader, 1);

            PdfContentByte contentByte = writer.getDirectContent();
            contentByte.addTemplate(page, 0, 0);

            Font font = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
            Paragraph paragraph = new Paragraph(30, cashReceipt, font);
            Paragraph empty = new Paragraph("\n".repeat(12));

            document.add(empty);
            document.add(paragraph);

            document.close();
            writer.close();

            log.info("uploadFilePdf {}", path);
        } catch (DocumentException | IOException e) {
            log.error(e.getMessage());
        }
        return path;
    }

}
