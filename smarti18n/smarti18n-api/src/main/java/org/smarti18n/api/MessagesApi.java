package org.smarti18n.api;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public interface MessagesApi {

    String PROJECT_ID_HEADER = "PROJECT_ID";
    String PROJECT_SECRET_HEADER = "PROJECT_SECRET";

    String PATH_MESSAGES_FIND_ALL = "/api/1/messages/findAll";
    String PATH_MESSAGES_FIND_SPRING = "/api/1/messages/findForSpringMessageSource";
    String PATH_MESSAGES_INSERT = "/api/1/messages/insert";
    String PATH_MESSAGES_UPDATE = "/api/1/messages/update";
    String PATH_MESSAGES_COPY = "/api/1/messages/copy";
    String PATH_MESSAGES_REMOVE = "/api/1/messages/remove";

    Collection<? extends Message> findAll(
            String projectId,
            String projectSecret
    );

    Map<String, Map<Locale, String>> findForSpringMessageSource(
            String projectId,
            String projectSecret
    );

    MessageImpl insert(
            String projectId,
            String projectSecret,
            String key
    );

    MessageImpl update(
            String projectId,
            String projectSecret,
            String key,
            String translation,
            Locale language
    );

    MessageImpl copy(
            String projectId,
            String projectSecret,
            String sourceKey,
            String targetKey
    );

    void remove(
            String projectId,
            String projectSecret,
            String key
    );
}
