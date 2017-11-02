package org.smarti18n.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageTranslations implements Serializable {

    private String key;
    private Map<Locale, String> translations;

    public MessageTranslations() {
    }

    public MessageTranslations(final String key) {
        this.key = key;
        this.translations = new HashMap<>();
    }

    public MessageTranslations(final String key, final Map<Locale, String> translations) {
        this.key = key;
        this.translations = translations;
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
}
