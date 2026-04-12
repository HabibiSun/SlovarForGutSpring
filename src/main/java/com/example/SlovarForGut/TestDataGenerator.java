package com.example.SlovarForGut;

import java.util.Arrays;
import java.util.List;

public class TestDataGenerator {

    public static void main(String[] args) {
        DictionaryRepository repository = new DictionaryRepository();
        DictionaryService service = new DictionaryService(repository);

        System.out.println("  ГЕНЕРАЦИЯ СЛОВАРЯ СЛОВ (mapWord.ser)  ");
        Dictionary wordDict = new Dictionary();

        addSafely(service, wordDict, "java", Arrays.asList("кофе", "остров", "язык"), DictionaryType.WORD.getDefaultFileName());
        addSafely(service, wordDict, "book", Arrays.asList("книга", "чтиво", "томик"), DictionaryType.WORD.getDefaultFileName());
        addSafely(service, wordDict, "hello", Arrays.asList("привет", "здравствуйте"), DictionaryType.WORD.getDefaultFileName());

        addSafely(service, wordDict, "cats", Arrays.asList(
                "кот", "кошка", "кот", "Кот", "мяу!", "кот123", "", null, "    "
        ), DictionaryType.WORD.getDefaultFileName());


        System.out.println("\n  ГЕНЕРАЦИЯ СЛОВАРЯ ЦИФР (mapNums.ser)  ");
        Dictionary numDict = new Dictionary();

        addSafely(service, numDict, "12345", Arrays.asList("один", "два", "три"), DictionaryType.NUMBER.getDefaultFileName());
        addSafely(service, numDict, "00000", Arrays.asList("ноль", "ничего"), DictionaryType.NUMBER.getDefaultFileName());

        addSafely(service, numDict, "99999", Arrays.asList(
                "МАКСИМУМ", "Hello", "привет мир", "дефис-тут", "АБВГД", "КОНЕЦ"
        ), DictionaryType.NUMBER.getDefaultFileName());

        System.out.println("\nТестовые данные успешно сгенерированы! (Ошибки выше — это успешная проверка валидации)");
    }


    private static void addSafely(DictionaryService service, Dictionary dictionary, String key, List<String> values, String fileName) {
        try {
            service.addElement(dictionary, key, values, fileName);
            System.out.println(" [+] Успешно добавлен ключ: " + key);
        } catch (IllegalArgumentException e) {
            System.out.println(" [-] Ошибка валидации при добавлении ключа [" + key + "]: " + e.getMessage());
        }
    }
}