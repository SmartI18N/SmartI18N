package org.smarti18n.api;

import java.util.Collection;
import java.util.Locale;

import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Message;

public interface MessagesApi {

    String PATH_MESSAGES_FIND_ALL = "/api/1/messages/findAll";
    String PATH_MESSAGES_FIND_ONE = "/api/1/messages/findOne";
    String PATH_MESSAGES_INSERT = "/api/1/messages/insert";
    String PATH_MESSAGES_UPDATE = "/api/1/messages/update";
    String PATH_MESSAGES_COPY = "/api/1/messages/copy";
    String PATH_MESSAGES_REMOVE = "/api/1/messages/remove";

    Collection<Message> findAll(
            String projectId
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message findOne(
            String projectId,
            String key
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message insert(
            String projectId,
            String key
    ) throws UserRightsException, MessageExistException, UserUnknownException, ProjectUnknownException;

    Message update(
            String projectId,
            String key,
            Locale locale,
            String translation
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message update(
            String projectId,
            Message message
    ) throws UserUnknownException, MessageUnknownException, UserRightsException, ProjectUnknownException;

    Message copy(
            String projectId,
            String sourceKey,
            String targetKey
    ) throws UserRightsException, MessageExistException, MessageUnknownException, UserUnknownException, ProjectUnknownException;

    void remove(
            String projectId,
            String key
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;
}
