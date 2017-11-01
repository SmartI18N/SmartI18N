package org.smarti18n.api;

import java.io.Serializable;
import java.util.Locale;

public class MessageSimple implements Serializable {

    private String key;
    private String translation;
    private Locale language;

    public MessageSimple() {
    }

    public MessageSimple(final String key, final String translation, final Locale language) {
        this.key = key;
        this.translation = translation;
        this.language = language;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(final String translation) {
        this.translation = translation;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(final Locale language) {
        this.language = language;
    }
}
