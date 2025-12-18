package ru.urfu.exepction;

/**
 * Исключение "объект не найден" для документа
 *
 * @author Daria
 */
public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(int index) {
        super(String.format("Документ с индексом %d не найден" , index));
    }
}
