package com.example.SlovarForGut;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class DictionaryService {

    private final DictionaryEntryRepository repository;
    private static final Pattern VALUE_PATTERN = Pattern.compile("[а-яА-Я]+");

    public DictionaryService(DictionaryEntryRepository repository) {
        this.repository = repository;
    }

    public List<DictionaryEntry> getAll(DictionaryType type) {
        return repository.findAllByType(type);
    }

    public Set<String> findElement(DictionaryType type, String key) {
        return repository.findByTypeAndKey(type, key)
                .map(DictionaryEntry::getValues)
                .orElse(Set.of()); // Если не нашли, возвращаем пустой Set
    }

    @Transactional
    public void addElement(DictionaryType type, String key, List<String> values) {
        // Проверяем, что значения содержат только русские буквы
        for (String val : values) {
            if (val == null || !VALUE_PATTERN.matcher(val).matches()) {
                throw new IllegalArgumentException("Значение '" + val + "' недопустимо! Разрешены только русские буквы.");
            }
        }

        // Ищем существующую запись в базе. Если нет — создаем новую
        DictionaryEntry entry = repository.findByTypeAndKey(type, key)
                .orElse(new DictionaryEntry(type, key));

        entry.getValues().addAll(values);
        repository.save(entry); // Сохраняем в БД (INSERT или UPDATE)
    }

    @Transactional
    public boolean removeElement(DictionaryType type, String key) {
        if (repository.findByTypeAndKey(type, key).isPresent()) {
            repository.deleteByTypeAndKey(type, key);
            return true;
        }
        return false;
    }

    public Optional<SearchResult> globalSearch(String key) {
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
}