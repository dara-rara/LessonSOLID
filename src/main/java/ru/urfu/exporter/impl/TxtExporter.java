package ru.urfu.exporter.impl;

import org.springframework.stereotype.Component;
import ru.urfu.exporter.Exporter;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Экспортёр текста в TXT
 */
@Component
public class TxtExporter implements Exporter {

    @Override
    public void export(Path outputPath, ru.urfu.model.Document document)
            throws java.io.IOException {
        Files.writeString(outputPath, document.content());
    }

    @Override
    public String getFormat() {
        return "txt";
    }
}
