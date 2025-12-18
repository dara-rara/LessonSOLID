package ru.urfu.service;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.exporter.ExporterRegistry;
import ru.urfu.model.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Сервис для экспорта документов
 *
 * @author Daria
 */
@Service
public class ExportService {

    private final ExporterRegistry exporterRegistry;
    private final Path outputDir = Path.of(System.getProperty("user.home"), "lessonSOLID");

    @Autowired
    public ExportService(ExporterRegistry exporterRegistry) {
        this.exporterRegistry = exporterRegistry;
    }

    /**
     * Экспортирует документ в выбранном формате
     *
     * @throws DocumentException   если произошла ошибка генерации
     * @throws IOException если не удалось записать файл
     */
    public Path exportDocument(String format, Document document) throws DocumentException, IOException {
        Path outputPath = buildOutputPath(document, format);
        exporterRegistry.getExporter(format).export(outputPath, document);
        return outputPath;
    }

    /**
     * Создает путь для сохранения экспортированного документа
     * @throws RuntimeException если не удалось создать директорию
     */
    private Path buildOutputPath(Document document, String format) {
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания директории: " + e.getMessage());
        }
        return outputDir.resolve(document.name() + "." + format);
    }

    /**
     * Возвращает обработанную строку форматов
     */
    public String getExportFormats() {
        return String.join("/", exporterRegistry.getFormats());
    }
}
