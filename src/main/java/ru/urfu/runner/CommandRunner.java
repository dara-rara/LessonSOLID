package ru.urfu.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.urfu.exepction.DocumentNotFoundException;
import ru.urfu.model.Document;
import ru.urfu.service.DocumentService;
import ru.urfu.service.ExportService;
import ru.urfu.service.ImportService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Командный обработчик для работы с документами
 * @author Daria
 */
@Component
public class CommandRunner implements CommandLineRunner {
    private final DocumentService documentService;
    private final ImportService importService;
    private final ExportService exportService;
    private final Scanner scanner;

    @Autowired
    public CommandRunner(DocumentService documentService,
                         ImportService importService,
                         ExportService exportService) {
        this.documentService = documentService;
        this.importService = importService;
        this.exportService = exportService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Основной цикл обработки команд
     */
    @Override
    public void run(String... args) {
        System.out.println("=== Консольное приложение ===");

        while (true) {
            System.out.println("\nКоманды: import, list, create, export, exit");
            System.out.print("> ");
            String cmd = scanner.nextLine().trim();

            try {
                if (!processCommand(cmd)) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Обрабатывает введенную команду
     */
    private boolean processCommand(String cmd) {
        return switch (cmd) {
            case "create" -> {
                createDocument();
                yield true;
            }
            case "import" -> {
                importDocument();
                yield true;
            }
            case "list" -> {
                listDocuments();
                yield true;
            }
            case "export" -> {
                exportDocument();
                yield true;
            }
            case "exit" -> false;
            default -> {
                System.out.println("Неизвестная команда");
                yield true;
            }
        };
    }

    /**
     * Создает новый документ через ввод в консоли
     */
    private void createDocument() {
        System.out.print("Введите имя документа: ");
        String name = scanner.nextLine().trim();

        System.out.println("Введите содержимое документа (пустая строка — завершить ввод):");
        StringBuilder content = new StringBuilder();

        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            content.append(line).append(System.lineSeparator());
        }

        documentService.createDocument(name, content.toString());
        System.out.println("Документ создан и сохранён в памяти");
    }

    /**
     * Импортирует документ из текстового файла
     * @throws IOException если файл не найден или недоступен для чтения
     */
    private void importDocument() {
        System.out.print("Введите путь к txt файлу: ");
        String pathStr = scanner.nextLine();
        try {
            Path path = Path.of(pathStr);
            String content = importService.getContentImportFile(path);
            documentService.createDocument(path.getFileName().toString(), content);
            System.out.println("Документ импортирован успешно");
        } catch (IOException e) {
            System.out.println("Ошибка импорта: " + e.getMessage());
        }
    }


    /**
     * Выводит список всех документов
     */
    private void listDocuments() {
        List<Document> documents = documentService.getDocuments();
        if (documents.isEmpty()) {
            System.out.println("Документов нет");
            return;
        }

        for (int i = 0; i < documents.size(); i++) {
            System.out.println(i + ": " + documents.get(i).name());
        }
    }

    /**
     * Экспортирует документ в указанный формат
     */
    private void exportDocument() {
        try {
            System.out.print("Введите номер документа: ");
            int index = Integer.parseInt(scanner.nextLine());

            if (index < 0) {
                throw new IllegalArgumentException("Номер не может быть отрицательным");
            }

            Document document = documentService.getDocument(index);

            System.out.print("Введите формат (" + exportService.getExportFormats() + "): ");
            String format = scanner.nextLine();

            if (format == null || format.trim().isEmpty()) {
                throw new IllegalArgumentException("Формат не может быть пустым");
            }

            Path outputPath = exportService.exportDocument(format, document);
            System.out.println("Экспорт выполнен: " + outputPath);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректный номер");
        } catch (DocumentNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный формат: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка экспорта: " + e.getMessage());
        }
    }
}