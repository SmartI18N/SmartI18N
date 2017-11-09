package org.smarti18n.api;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = MessageImpl.class)
public interface Message extends Serializable {

    String getKey();

    void setKey(String key);

    Map<Locale, String> getTranslations();

    void setTranslations(Map<Locale, String> translations);

    default String getTranslation(Locale language) {
        final Map<Locale, String> translations = getTranslations();
        if (translations == null) {
            return null;
        }
        return translations.getOrDefault(language, "");
    }

    default Set<Locale> getLanguages() {
        return getTranslations().entrySet().stream()
                .filter(localeStringEntry -> !StringUtils.isEmpty(localeStringEntry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    default String getLanguagesAsString() {
        final StringBuilder builder = new StringBuilder();
        getLanguages().forEach(locale -> {
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(locale.getLanguage());
        });
        return builder.toString();
    }
}
