package org.smarti18n.messages.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Document
public class MessageEntity {

    @Id
    private String key;

    private Map<Locale, String> translations;

    MessageEntity() {
    }

    public MessageEntity(final String key) {
        this.key = key;
        this.translations = new HashMap<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public Map<Locale, String> getTranslations() {
        return translations;
    }

    public void setTranslations(final Map<Locale, String> translations) {
        this.translations = translations;
    }

    public void putTranslation(final Locale language, final String translation) {
        this.translations.put(language, translation);
    }

    public String getTranslation(final Locale language) {
        return translations.get(language);
    }
}
