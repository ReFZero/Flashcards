package pl.ReFZero.model;

public enum LanguageType {
    POLISH("polish"), ENGLISH("english"), GERMAN("german"), SPANISH("spanish"), NORWEGIAN("norwegian");

    private final String language;

    LanguageType(String language) {
        this.language = language;
    }

    public String getStringValue() {
        return language;
    }
}
