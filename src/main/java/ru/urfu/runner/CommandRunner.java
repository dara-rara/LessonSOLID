package ru.urfu.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.urfu.exepction.DocumentNotFoundException;
import ru.urfu.exporter.ExporterRegistry;
import ru.urfu.model.Document;
import ru.urfu.service.DocumentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * @author Daria
 */
@Component
public class CommandRunner implements CommandLineRunner {
    private final DocumentService documentService;
    private final ExporterRegistry exporterRegistry;
    private final Scanner scanner;

    public CommandRunner(DocumentService documentService, ExporterRegistry exporterRegistry) {
        this.documentService = documentService;
        this.exporterRegistry = exporterRegistry;
        this.scanner = new Scanner(System.in);
    }

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

    private void createDocument() {
        System.out.print("Введите имя документа: ");
        String name = scanner.nextLine().trim();

        System.out.println("Введите содержимое документа (пустая строка — завершить ввод):");
        StringBuilder content = new StringBuilder();

        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) break;
            content.append(line).append(System.lineSeparator());
        }

        documentService.createDocument(name, content.toString());
        System.out.println("Документ создан и сохранён в памяти");
    }

    private void importDocument() {
        System.out.print("Введите путь к txt файлу: ");
        String pathStr = scanner.nextLine();
        try {
            Path path = Path.of(pathStr);
            if (!Files.exists(path)) {
                throw new IOException("Файл не найден: " + path);
            }
            String content = Files.readString(path);
            documentService.createDocument(path.getFileName().toString(), content);
            System.out.println("Документ импортирован успешно");
        } catch (IOException e) {
            System.out.println("Ошибка импорта: " + e.getMessage());
        }
    }

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

    private void exportDocument() {
        try {
            System.out.print("Введите номер документа: ");
            int index = Integer.parseInt(scanner.nextLine());

            if (index < 0) {
                throw new IllegalArgumentException("Номер не может быть отрицательным");
            }

            Document document = documentService.getDocument(index);

            System.out.print("Введите формат (" + String.join("/", exporterRegistry.getFormats()) + "): ");
            String format = scanner.nextLine();

            if (format == null || format.trim().isEmpty()) {
                throw new IllegalArgumentException("Формат не может быть пустым");
            }

            exporterRegistry.getExporter(format).export(buildOutputPath(document, format), document);
            System.out.println("Экспорт выполнен: " + buildOutputPath(document, format));
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

    private Path buildOutputPath(Document document, String format) {
        Path outputDir = Path.of(System.getProperty("user.home"), "lessonSOLID");
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания директории: " + e.getMessage());
        }
        return outputDir.resolve(document.name() + "." + format);
    }
}
