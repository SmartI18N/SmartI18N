package org.smarti18n.api;

import java.util.Collection;
import java.util.Locale;

public interface MessagesApi {

    String PATH_MESSAGES_FIND_ALL = "/api/1/messages/findAll";
    String PATH_MESSAGES_FIND_ONE = "/api/1/messages/findOne";
    String PATH_MESSAGES_INSERT = "/api/1/messages/insert";
    String PATH_MESSAGES_UPDATE = "/api/1/messages/update";
    String PATH_MESSAGES_COPY = "/api/1/messages/copy";
    String PATH_MESSAGES_REMOVE = "/api/1/messages/remove";

    Collection<? extends Message> findAll(
            String projectId
    );

    Message findOne(
            String projectId,
            String key
    );

    MessageImpl insert(
            String projectId,
            String key
    );

    MessageImpl update(
            String projectId,
            String key,
            Locale locale,
            String translation
    );

    MessageImpl copy(
            String projectId,
            String sourceKey,
            String targetKey
    );

    void remove(
            String projectId,
            String key
    );
}
