package com.example.SlovarForGut;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/dictionary")
@Tag(name = "Управление словарями", description = "API для добавления, поиска и удаления записей")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    private DictionaryType getType(String type) {
        try {
            return DictionaryType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный тип словаря. Доступные типы: WORD, NUMBER");
        }
    }


    // GET http://localhost:8080/api/dictionary/search/java
    @Operation(summary = "Глобальный поиск", description = "Ищет ключ сразу во всех доступных словарях.")
    @GetMapping("/search/{key}")
    public ResponseEntity<?> searchGlobally(
            @Parameter(description = "Ключ для поиска (например: java или 12345)") @PathVariable String key) {
        Optional<SearchResult> result = dictionaryService.globalSearch(key);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ключ '" + key + "' не найден ни в одном словаре или не соответствует их форматам.");
        }
    }

    // GET http://localhost:8080/api/dictionary/word
    @Operation(summary = "Получить весь словарь", description = "Возвращает список всех записей указанного словаря.")
    @GetMapping("/{type}")
    public ResponseEntity<?> getAllElements(
            @Parameter(description = "Тип словаря (word или number)") @PathVariable String type) {
        try {
            DictionaryType dictType = getType(type);
            List<DictionaryEntry> elements = dictionaryService.getAll(dictType);
            return ResponseEntity.ok(elements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET http://localhost:8080/api/dictionary/word/java
    @Operation(summary = "Найти элемент", description = "Ищет конкретный ключ в конкретном словаре.")
    @GetMapping("/{type}/{key}")
    public ResponseEntity<?> getElement(
            @Parameter(description = "Тип словаря (word или number)") @PathVariable String type,
            @Parameter(description = "Ключ") @PathVariable String key) {
        try {
            DictionaryType dictType = getType(type);
            Set<String> values = dictionaryService.findElement(dictType, key);

            if (values.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ключ не найден");
            }
            return ResponseEntity.ok(values);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST http://localhost:8080/api/dictionary/word
    @Operation(summary = "Добавить запись", description = "Добавляет новый ключ и список значений в словарь.")
    @PostMapping("/{type}")
    public ResponseEntity<?> addElement(
            @Parameter(description = "Тип словаря (word или number)") @PathVariable String type,
            @RequestBody DictionaryRequest request) {
        try {
            DictionaryType dictType = getType(type);

            if (request.getKey() == null || !request.getKey().matches(dictType.getKeyRegex())) {
                return ResponseEntity.badRequest().body("Ошибка: Ключ не соответствует формату! Правило: " + dictType.getFormatDescription());
            }

            dictionaryService.addElement(dictType, request.getKey(), request.getValues());
            return ResponseEntity.ok("Запись успешно добавлена");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE http://localhost:8080/api/dictionary/number/12345
    @Operation(summary = "Удалить запись", description = "Удаляет ключ и все его значения из словаря.")
    @DeleteMapping("/{type}/{key}")
    public ResponseEntity<?> removeElement(
            @Parameter(description = "Тип словаря (word или number)") @PathVariable String type,
            @Parameter(description = "Ключ для удаления") @PathVariable String key) {
        try {
            DictionaryType dictType = getType(type);
            boolean removed = dictionaryService.removeElement(dictType, key);

            if (removed) {
                return ResponseEntity.ok("Запись успешно удалена");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ключ не найден");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}