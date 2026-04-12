package com.example.SlovarForGut;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class DictionaryService {

    private final DictionaryRepository repository;

    public DictionaryService(DictionaryRepository repository) {
        this.repository = repository;
    }

    public Dictionary loadDictionary(String fileName) {
        return repository.load(fileName);
    }

    public void addElement(Dictionary dictionary, String key, List<String> values, String fileName) {
        dictionary.put(key, values);
        repository.save(dictionary, fileName);
    }

    public boolean removeElement(Dictionary dictionary, String key, String fileName) {
        if (dictionary.remove(key)) {
            repository.save(dictionary, fileName);
            return true;
        }
        return false;
    }

    public Set<String> findElement(Dictionary dictionary, String key) {
        return dictionary.get(key);
    }
}
