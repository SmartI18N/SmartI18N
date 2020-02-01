package org.smarti18n.models;

import java.util.Locale;
import java.util.Map;

public class MessageUpdateDTO {

    private Map<Locale, String> translations;

    public MessageUpdateDTO() {
    }

    public MessageUpdateDTO(Map<Locale, String> translations) {
        this.translations = translations;
    }

    public Map<Locale, String> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<Locale, String> translations) {
        this.translations = translations;
    }
}
