package org.smarti18n.api;

import java.util.Collection;
import java.util.Locale;

public interface MessagesApi {

    String PATH_ROOT = "/api/1";
    String PATH_FIND_ONE = "/api/1/findOne";
    String PATH_FIND_ALL = "/api/1/findAll";
    String PATH_INSERT = "/api/1/insert";
    String PATH_SAVE = "/api/1/save";
    String PATH_REMOVE = "/api/1/remove";

    String root();

    MessageSimple findOne(
            String key,
            Locale language
    );

    Collection<MessageTranslations> findAll();

    MessageTranslations insert(
            String key
    );

    MessageTranslations save(
            String key,
            String translation,
            Locale language
    );

    void remove(
            String key
    );
}
