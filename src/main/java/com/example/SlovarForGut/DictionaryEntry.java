package com.example.SlovarForGut;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dictionary_entries")
public class DictionaryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dict_type", nullable = false)
    private DictionaryType type;

    @Column(name = "dict_key", nullable = false)
    private String key;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dictionary_values", joinColumns = @JoinColumn(name = "entry_id"))
    @Column(name = "dict_value")
    private Set<String> values = new HashSet<>();

    public DictionaryEntry() {}

    public DictionaryEntry(DictionaryType type, String key) {
        this.type = type;
        this.key = key;
    }

    public Long getId() { return id; }
    public DictionaryType getType() { return type; }
    public String getKey() { return key; }
    public Set<String> getValues() { return values; }
    public void setValues(Set<String> values) { this.values = values; }
}
