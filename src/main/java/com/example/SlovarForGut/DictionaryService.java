package com.example.SlovarForGut;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.Set;

@Service
public class DictionaryService {

    private final DictionaryRepository repository;

    public DictionaryService(DictionaryRepository repository) {
        this.repository = repository;
    }

    public synchronized List<Dictionary.Element> getAll(DictionaryType type) {
        return repository.load(type.getDefaultFileName()).getAll();
    }

    public synchronized Optional<SearchResult> globalSearch(String key) {
        for (DictionaryType type : DictionaryType.values()) {
            if (key.matches(type.getKeyRegex())) {
                Set<String> values = findElement(type, key);
                if (!values.isEmpty()) {
                    return Optional.of(new SearchResult(type.name(), type.getName(), values));
                }
            }
        }
        return Optional.empty();
    }

    public synchronized Set<String> findElement(DictionaryType type, String key) {
        return repository.load(type.getDefaultFileName()).get(key);
    }

    public synchronized void addElement(DictionaryType type, String key, List<String> values) {
        Dictionary dictionary = repository.load(type.getDefaultFileName());
        dictionary.put(key, values);
        repository.save(dictionary, type.getDefaultFileName());
    }

    public synchronized boolean removeElement(DictionaryType type, String key) {
        Dictionary dictionary = repository.load(type.getDefaultFileName());
        if (dictionary.remove(key)) {
            repository.save(dictionary, type.getDefaultFileName());
            return true;
        }
        return false;
    }
}