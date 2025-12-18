package ru.urfu.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Реестр экспортеров для управления доступными форматами экспорта
 *
 * @author Daria
 */
@Component
public class ExporterRegistry {
    private final Map<String, Exporter> exporters;

    @Autowired
    public ExporterRegistry(List<Exporter> exporterList) {
        this.exporters = exporterList.stream()
                .collect(Collectors.toMap(Exporter::getFormat, Function.identity()));
    }

    /**
     * Возвращает экспортер для указанного формата
     * @throws IllegalArgumentException если формат не поддерживается
     */
    public Exporter getExporter(String format) {
        Exporter exporter = exporters.get(format.toLowerCase().trim());
        if (exporter == null) {
            throw new IllegalArgumentException("Неизвестный формат: " + format);
        }
        return exporter;
    }

    /**
     * Возвращает список поддерживаемых форматов
     */
    public List<String> getFormats() {
        return List.copyOf(exporters.keySet());
    }
}