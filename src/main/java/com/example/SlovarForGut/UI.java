package com.example.SlovarForGut;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

@Component
public class UI implements CommandLineRunner {

    private final Scanner scanner;
    private final DictionaryService dictionaryService;

    public UI(Scanner scanner, DictionaryService dictionaryService) {
        this.scanner = scanner;
        this.dictionaryService = dictionaryService;
    }

    @Override
    public void run(String... args) {
        start();
    }

    public void start() {
        while (true) {
            System.out.println("\nГЛАВНОЕ МЕНЮ");
            System.out.println("1. Словарь со словами");
            System.out.println("2. Словарь с цифрами");
            System.out.println("0. Выход");
            System.out.print("Выберите пункт: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleDictionary(DictionaryType.WORD);
                case "2" -> handleDictionary(DictionaryType.NUMBER);
                case "0" -> {
                    System.out.println("Завершение программы...");
                    System.exit(0);
                }
                default -> System.out.println("Неверный ввод. Попробуйте снова.");
            }
        }
    }

    private void handleDictionary(DictionaryType type) {
        System.out.print("Введите имя файла (без .ser) [По умолчанию: " + type.getDefaultFileName() + "]: ");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) {
            fileName = type.getDefaultFileName();
        }

        Dictionary dictionary = dictionaryService.loadDictionary(fileName);
        System.out.println("Загружен " + type.getName() + " из " + fileName + ".ser");

        dictionaryMenuLoop(dictionary, type, fileName);
    }

    private void dictionaryMenuLoop(Dictionary dictionary, DictionaryType type, String fileName) {
        while (true) {
            System.out.println("\nОПЕРАЦИИ СО СЛОВАРЕМ");
            System.out.println("1. Найти по ключу");
            System.out.println("2. Добавить элемент");
            System.out.println("3. Удалить ключ");
            System.out.println("4. Посмотреть содержимое");
            System.out.println("0. Вернуться в главное меню");
            System.out.print("Выберите пункт: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> findElement(dictionary);
                case "2" -> addElement(dictionary, type, fileName);
                case "3" -> removeElement(dictionary, fileName);
                case "4" -> showDictionary(dictionary);
                case "0" -> { return; }
                default -> System.out.println("Неверный ввод. Попробуйте снова.");
            }
        }
    }

    private void findElement(Dictionary dictionary) {
        System.out.println("\nПОИСК ЭЛЕМЕНТА");
        while (true) {
            System.out.print("Введите ключ для поиска (или '0' для отмены): ");
            String key = scanner.nextLine().trim();

            if (key.equals("0")) {
                System.out.println("Операция отменена.");
                return;
            }

            Set<String> values = dictionaryService.findElement(dictionary, key);

            if (values.isEmpty()) {
                System.out.println("Ошибка: Ключ [" + key + "] не найден. Попробуйте другой.");
            } else {
                System.out.println("Успех! Значения: " + String.join("; ", values));
                break;
            }
        }
    }

    private void addElement(Dictionary dictionary, DictionaryType type, String fileName) {
        System.out.println("\nДОБАВЛЕНИЕ НОВОГО ЭЛЕМЕНТА");
        String key = "";

        while (true) {
            System.out.println("Правило для ключа: " + type.getFormatDescription());
            System.out.print("Введите ключ (или '0' для отмены): ");
            key = scanner.nextLine().trim();

            if (key.equals("0")) {
                System.out.println("Операция отменена.");
                return;
            }

            if (!key.matches(type.getKeyRegex())) {
                System.out.println("Ошибка: Ключ не соответствует формату! Попробуйте еще раз.\n");
            } else {
                break;
            }
        }


        while (true) {
            System.out.print("Введите значения через пробел (только кириллица) или '0' для отмены: ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                System.out.println("Операция отменена.");
                return;
            }

            String[] values = input.split("\\s+");

            try {
                dictionaryService.addElement(dictionary, key, Arrays.asList(values), fileName);
                System.out.println("Успех: Запись [" + key + "] успешно сохранена.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка добавления: " + e.getMessage() + " Попробуйте еще раз.\n");
            }
        }
    }

    private void removeElement(Dictionary dictionary, String fileName) {
        System.out.println("\nУДАЛЕНИЕ КЛЮЧА");
        while (true) {
            System.out.print("Введите ключ для удаления (или '0' для отмены): ");
            String key = scanner.nextLine().trim();

            if (key.equals("0")) {
                System.out.println("Операция отменена.");
                return;
            }

            if (dictionaryService.removeElement(dictionary, key, fileName)) {
                System.out.println("Успех: Запись [" + key + "] удалена из словаря.");
                break;
            } else {
                System.out.println("Ошибка: Ключ [" + key + "] не найден в словаре. Попробуйте еще раз.");
            }
        }
    }

    private void showDictionary(Dictionary dictionary) {
        System.out.println("\nСОДЕРЖИМОЕ СЛОВАРЯ");
        var data = dictionary.getAll();

        if (data.isEmpty()) {
            System.out.println("Словарь пуст.");
            return;
        }

        data.forEach(element ->
                System.out.println(element.getKey() + ": " + String.join("; ", element.getValues()))
        );
    }
}