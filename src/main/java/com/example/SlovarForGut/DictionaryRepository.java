package com.example.SlovarForGut;
import org.springframework.stereotype.Repository;

import java.io.*;

@Repository
public class DictionaryRepository {
    private static final String EXTENSION = ".ser";

    public void save(Dictionary dictionary, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName + EXTENSION))) {
            oos.writeObject(dictionary);
            System.out.println("Словарь успешно сохранен в " + fileName + EXTENSION);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    public Dictionary load(String fileName) {
        File file = new File(fileName + EXTENSION);
        if (!file.exists() || file.isDirectory()) {
            return new Dictionary();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Dictionary) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке файла: " + e.getMessage());
            return new Dictionary();
        }
    }
}
