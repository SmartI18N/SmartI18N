package org.smarti18n.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

@JsonDeserialize(as = MessageImpl.class)
public interface Message extends Serializable {

    String getKey();

    void setKey(String key);

    Map<Locale, String> getTranslations();

    void setTranslations(Map<Locale, String> translations);
}
