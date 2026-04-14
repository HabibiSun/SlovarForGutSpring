package com.example.SlovarForGut;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TestDataGenerator {

    public static void main(String[] args) {
        cleanUpOldFiles();

        DictionaryRepository repository = new DictionaryRepository();
        DictionaryService service = new DictionaryService(repository);

        System.out.println("ГЕНЕРАЦИЯ СЛОВАРЯ СЛОВ");
        addSafely(service, DictionaryType.WORD, "java", Arrays.asList("кофе", "остров", "язык"));
        addSafely(service, DictionaryType.WORD, "book", Arrays.asList("книга", "чтиво", "томик"));
        addSafely(service, DictionaryType.WORD, "hello", Arrays.asList("привет", "здравствуйте"));

        addSafely(service, DictionaryType.WORD, "cats", Arrays.asList(
                "кот", "кошка", "кот", "Кот", "мяу!", "кот123", "", null, "    "
        ));

        System.out.println("\nГЕНЕРАЦИЯ СЛОВАРЯ ЦИФР");
        addSafely(service, DictionaryType.NUMBER, "12345", Arrays.asList("один", "два", "три"));
        addSafely(service, DictionaryType.NUMBER, "00000", Arrays.asList("ноль", "ничего"));

        addSafely(service, DictionaryType.NUMBER, "99999", Arrays.asList(
                "МАКСИМУМ", "Hello", "привет мир", "дефис-тут", "АБВГД", "КОНЕЦ"
        ));

        System.out.println("\nТестовые данные успешно сгенерированы!");
    }

    private static void addSafely(DictionaryService service, DictionaryType type, String key, List<String> values) {
        try {
            if (key == null || !key.matches(type.getKeyRegex())) {
                throw new IllegalArgumentException("Ключ не соответствует формату! Правило: " + type.getFormatDescription());
            }

            service.addElement(type, key, values);
            System.out.println("Успешно добавлен ключ: " + key);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка валидации при добавлении ключа [" + key + "]: " + e.getMessage());
        }
    }

    private static void cleanUpOldFiles() {
        boolean deletedWord = new File(DictionaryType.WORD.getDefaultFileName() + ".ser").delete();
        boolean deletedNum = new File(DictionaryType.NUMBER.getDefaultFileName() + ".ser").delete();

        if (deletedWord || deletedNum) {
            System.out.println("Старые файлы словарей удалены.\n");
        }
    }
}