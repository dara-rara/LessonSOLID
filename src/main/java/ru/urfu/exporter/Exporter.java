package ru.urfu.exporter;

import com.itextpdf.text.DocumentException;
import ru.urfu.model.Document;

import java.nio.file.Path;

/**
 * Экспортёр документа в файл
 *
 * @author Daria
 */
public interface Exporter {

    /**
     * Экспортирует документ в файл
     *
     * @throws DocumentException   если произошла ошибка генерации
     * @throws java.io.IOException если не удалось записать файл
     */
    void export(Path outputPath, Document document) throws DocumentException, java.io.IOException;

    /**
     * Возвращает формат
     */
    String getFormat();

}
