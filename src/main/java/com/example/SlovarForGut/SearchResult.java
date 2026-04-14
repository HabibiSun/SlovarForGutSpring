package com.example.SlovarForGut;

import java.util.Set;

public class SearchResult {
    private final String dictionaryType;
    private final String dictionaryName;
    private final Set<String> values;

    public SearchResult(String dictionaryType, String dictionaryName, Set<String> values) {
        this.dictionaryType = dictionaryType;
        this.dictionaryName = dictionaryName;
        this.values = values;
    }

    public String getDictionaryType() { return dictionaryType; }
    public String getDictionaryName() { return dictionaryName; }
    public Set<String> getValues() { return values; }
}
