package ru.urfu.service;

import org.springframework.stereotype.Service;
import ru.urfu.exepction.DocumentNotFoundException;
import ru.urfu.model.Document;

import java.util.*;

/**
 * Сервис для управления документами
 * Позволяет хранить документы в оперативной памяти
 */
@Service
public class DocumentService {

    private final List<Document> documents = new ArrayList<>();

    /**
     * Возвращает список всех импортированных документов
     */
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    /**
     * Возвращает документ по индексу
     * @throws DocumentNotFoundException если индекс вне диапазона
     */
    public Document getDocument(int index) {
        if (index < 0 || index >= documents.size()) {
            throw new DocumentNotFoundException(index);
        }
        return documents.get(index);
    }

    /**
     * Создаёт документ по имени и содержимому и сохраняет его в памяти
     */
    public void createDocument(String name, String content) {
        Document document = new Document(name, content);
        documents.add(document);
    }

}