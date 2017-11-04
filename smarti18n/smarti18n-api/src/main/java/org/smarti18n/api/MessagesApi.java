package org.smarti18n.api;

import java.util.Collection;
import java.util.Locale;

public interface MessagesApi {

    String PATH_FIND_ALL = "/api/1/findAll";
    String PATH_INSERT = "/api/1/insert";
    String PATH_SAVE = "/api/1/save";
    String PATH_COPY = "/api/1/copy";
    String PATH_REMOVE = "/api/1/remove";

    Collection<MessageTranslations> findAll();

    MessageTranslations insert(
            String key
    );

    MessageTranslations save(
            String key,
            String translation,
            Locale language
    );

    MessageTranslations copy(
            String sourceKey,
            String targetKey
    );

    void remove(
            String key
    );
}
