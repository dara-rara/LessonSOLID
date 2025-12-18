package ru.urfu.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Daria
 */
@Service
public class ImportService {

    /**
     * Получить содержимое импортированного файла
     *
     * @throws IOException если произошла ошибка при чтении файла
     */
    public String getContentImportFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new IOException("Файл не найден: " + path);
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IOException("Ошибка чтения файла: " + path);
        }
    }
}
