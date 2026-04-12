package com.example.SlovarForGut;

public enum DictionaryType {
    WORD("Словарь слов", "[a-z]{4}", "mapWord", "4 строчные латинские буквы (например: java, book)"),
    NUMBER("Словарь цифр", "\\d{5}", "mapNums", "ровно 5 цифр (например: 12345, 00000)");

    private final String name;
    private final String keyRegex;
    private final String defaultFileName;
    private final String formatDescription;

    DictionaryType(String name, String keyRegex, String defaultFileName, String formatDescription) {
        this.name = name;
        this.keyRegex = keyRegex;
        this.defaultFileName = defaultFileName;
        this.formatDescription = formatDescription;
    }

    public String getName() { return name; }
    public String getKeyRegex() { return keyRegex; }
    public String getDefaultFileName() { return defaultFileName; }
    public String getFormatDescription() { return formatDescription; }
}