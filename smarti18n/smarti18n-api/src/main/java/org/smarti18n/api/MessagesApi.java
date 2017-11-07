package org.smarti18n.api;

import java.util.Collection;
import java.util.Locale;

public interface MessagesApi {

    String PROJECT_ID_HEADER = "PROJECT_ID";
    String PROJECT_SECRET_HEADER = "PROJECT_SECRET";

    String PATH_MESSAGES_FIND_ALL = "/api/1/messages/findAll";
    String PATH_MESSAGES_INSERT = "/api/1/messages/insert";
    String PATH_MESSAGES_UPDATE = "/api/1/messages/update";
    String PATH_MESSAGES_COPY = "/api/1/messages/copy";
    String PATH_MESSAGES_REMOVE = "/api/1/messages/remove";

    Collection<MessageImpl> findAll();

    MessageImpl insert(
            String key
    );

    MessageImpl update(
            String key,
            String translation,
            Locale language
    );

    MessageImpl copy(
            String sourceKey,
            String targetKey
    );

    void remove(
            String key
    );
}
