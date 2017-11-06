package org.smarti18n.messages.entities;

import org.smarti18n.api.Message;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Document
public class MessageEntity implements Message {

    @Id
    private String key;

    private Map<Locale, String> translations;

    MessageEntity() {
    }

    public MessageEntity(final String key) {
        this.key = key;
        this.translations = new HashMap<>();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(final String key) {
        this.key = key;
    }

    @Override
    public Map<Locale, String> getTranslations() {
        return translations;
    }

    @Override
    public void setTranslations(final Map<Locale, String> translations) {
        this.translations = translations;
    }

    public void putTranslation(final Locale language, final String translation) {
        this.translations.put(language, translation);
    }
}
