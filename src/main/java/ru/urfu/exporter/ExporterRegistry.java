package ru.urfu.exporter;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daria
 */
@Component
public class ExporterRegistry {
    private final Map<String, Exporter> exporters;

    public ExporterRegistry(List<Exporter> exporterList) {
        this.exporters = exporterList.stream()
                .collect(Collectors.toMap(Exporter::getFormat, Function.identity()));
    }

    public Exporter getExporter(String format) {
        Exporter exporter = exporters.get(format.toLowerCase().trim());
        if (exporter == null) {
            throw new IllegalArgumentException("Неизвестный формат: " + format);
        }
        return exporter;
    }

    public List<String> getFormats() {
        return new ArrayList<>(exporters.keySet());
    }
}
