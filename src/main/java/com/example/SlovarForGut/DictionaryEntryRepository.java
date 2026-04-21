package com.example.SlovarForGut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryEntryRepository extends JpaRepository<DictionaryEntry, Long> {


    Optional<DictionaryEntry> findByTypeAndKey(DictionaryType type, String key);

    List<DictionaryEntry> findAllByType(DictionaryType type);

    void deleteByTypeAndKey(DictionaryType type, String key);
}
