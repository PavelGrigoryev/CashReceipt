package ru.clevertec.cashreceipt.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.service.UploadFileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public Path uploadFileTxt(String cashReceipt) {
        Path path = Paths.get("src/main/resources/txt/CashReceipt.txt");
        try {
            Path file = Files.write(path, cashReceipt.getBytes());
            log.info("uploadFileTxt {}", file);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return path;
    }

    @Override
    public Path uploadFilePdf(String cashReceipt) {
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
