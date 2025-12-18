package ru.urfu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс для запуска консольного приложения
 */
@SpringBootApplication
public class ConsoleApp {
    /**
     * Точка входа приложения
     */
    public static void main(String[] args) {
        SpringApplication.run(ConsoleApp.class, args);
    }
}
