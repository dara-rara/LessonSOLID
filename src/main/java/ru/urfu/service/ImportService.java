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
     * Получить путь для импорта файла
     *
     * @throws IOException если путь не существует
     */
    public Path getImportPath(String pathStr) throws IOException {
        Path path = Path.of(pathStr);
        if (!Files.exists(path)) {
            throw new IOException("Файл не найден: " + path);
        }
        return path;
    }

    /**
     * Получить содержимое импортированного файла
     *
     * @throws IOException если произошла ошибка при чтении файла
     */
    public String getContentImportFile(Path path) throws IOException {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IOException("Ошибка чтения файла: " + path);
        }
    }
}
