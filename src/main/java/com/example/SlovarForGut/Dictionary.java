package com.example.SlovarForGut;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

public class Dictionary implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Pattern VALUE_PATTERN = Pattern.compile("[а-яА-Я]+");

    public static class Element implements Serializable, Comparable<Element> {
        private final String key;

        private final Set<String> values;

        public Element(String key) {
            this.key = key;
            this.values = new TreeSet<>();
        }

        public String getKey() { return key; }

        public Set<String> getValues() {
            return Collections.unmodifiableSet(values);
        }

        public void addValues(List<String> newValues) {
            for (String val : newValues) {
                if (val == null || !VALUE_PATTERN.matcher(val).matches()) {
                    throw new IllegalArgumentException("Значение '" + val + "' недопустимо! Разрешены только русские буквы (без пробелов и цифр).");
                }
            }

            this.values.addAll(newValues);
        }

        @Override
        public int compareTo(Element other) {
            return this.key.compareTo(other.key);
        }
    }

    private final List<Element> entries = new ArrayList<>();

    public void put(String key, List<String> values) {
        Element dummySearchElement = new Element(key);
        int index = Collections.binarySearch(entries, dummySearchElement);

        if (index >= 0) {
            entries.get(index).addValues(values);
        } else {
            int insertionPoint = -(index + 1);
            Element newElement = new Element(key);
            newElement.addValues(values);
            entries.add(insertionPoint, newElement);
        }
    }

    public Set<String> get(String key) {
        int index = Collections.binarySearch(entries, new Element(key));
        if (index >= 0) {
            return entries.get(index).getValues();
        }
        return Collections.emptySet();
    }

    public boolean remove(String key) {
        int index = Collections.binarySearch(entries, new Element(key));
        if (index >= 0) {
            entries.remove(index);
            return true;
        }
        return false;
    }

    public List<Element> getAll() {
        return Collections.unmodifiableList(entries);
    }
}
