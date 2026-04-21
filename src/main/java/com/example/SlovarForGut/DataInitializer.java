package com.example.SlovarForGut;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DictionaryService service;

    public DataInitializer(DictionaryService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Заполнение базы данных тестовыми данными ===");

        try {
            service.addElement(DictionaryType.WORD, "java", Arrays.asList("кофе", "остров", "язык"));
            service.addElement(DictionaryType.WORD, "book", Arrays.asList("книга", "чтиво"));
            service.addElement(DictionaryType.NUMBER, "12345", Arrays.asList("один", "два", "три"));
            System.out.println("База данных успешно заполнена!");
        } catch (Exception e) {
            System.out.println("Ошибка при заполнении: " + e.getMessage());
        }
    }
}