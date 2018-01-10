package org.smarti18n.models;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageImpl implements Message {

    private String key;
    private Map<Locale, String> translations;

    public MessageImpl() {
        this.translations = new HashMap<>();
    }

    public MessageImpl(final Message message) {
        this.key = message.getKey();
        this.translations = new HashMap<>(
                message.getTranslations()
        );
    }

    public MessageImpl(final String key, final Map<Locale, String> translations) {
        this.key = key;
        this.translations = translations;
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

    public void putTranslation(final Locale value) {
        translations.putIfAbsent(value, "");
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageImpl)) {
            return false;
        }

        final MessageImpl message = (MessageImpl) o;

        return key.equals(message.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
