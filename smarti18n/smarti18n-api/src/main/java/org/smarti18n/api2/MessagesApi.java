package org.smarti18n.api2;

import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Message;
import org.smarti18n.models.MessageCreateDTO;
import org.smarti18n.models.MessageUpdateDTO;
import org.smarti18n.models.SingleMessageUpdateDTO;

import java.util.Collection;
import java.util.Locale;

public interface MessagesApi {

    String PATH_MESSAGES_FIND_ALL = "/api/2/projects/{projectId}/messages";
    String PATH_MESSAGES_FIND_ONE = "/api/2/projects/{projectId}/messages/{messageKey}";
    String PATH_MESSAGES_CREATE = "/api/2/projects/{projectId}/messages";
    String PATH_MESSAGES_UPDATE = "/api/2/projects/{projectId}/messages/{messageKey}";
    String PATH_MESSAGES_UPDATE_SINGLE = "/api/2/projects/{projectId}/messages/{messageKey}/locale/{locale}";
    String PATH_MESSAGES_REMOVE = "/api/2/projects/{projectId}/messages/{messageKey}";

    Collection<Message> findAll(
            String projectId
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message findOne(
            String projectId,
            String messageKey
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message create(
            String projectId,
            MessageCreateDTO dto
    ) throws UserRightsException, MessageExistException, UserUnknownException, ProjectUnknownException;

    Message update(
            String projectId,
            String messageKey,
            MessageUpdateDTO dto
    ) throws UserUnknownException, MessageUnknownException, UserRightsException, ProjectUnknownException;

    Message update(
            String projectId,
            String messageKey,
            String locale,
            SingleMessageUpdateDTO dto
    ) throws UserUnknownException, MessageUnknownException, UserRightsException, ProjectUnknownException;

    void remove(
            String projectId,
            String messageKey
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message copy(
            String projectId,
            String sourceMessageKey,
            String targetMessageKey
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;
}
