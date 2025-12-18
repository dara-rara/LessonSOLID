package ru.urfu.exporter.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ru.urfu.exporter.Exporter;

import java.io.FileOutputStream;
import java.nio.file.Path;

/**
 * Экспортёр текста в PDF
 */
@Component
public class PdfExporter implements Exporter {

    @Override
    public void export(Path outputPath, ru.urfu.model.Document document)
            throws DocumentException, java.io.IOException {

        try (FileOutputStream outputStream = new FileOutputStream(outputPath.toString())) {
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, outputStream);

            pdf.open();
            pdf.add(new Paragraph(document.content()));
            pdf.close();
        }
    }

    @Override
    public String getFormat() {
        return "pdf";
    }
}
