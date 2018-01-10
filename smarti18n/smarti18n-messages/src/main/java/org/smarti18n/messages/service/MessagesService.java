package org.smarti18n.messages.service;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Message;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface MessagesService {

    Collection<Message> findAll(
            String username,
            String projectId
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message findOne(
            String username,
            String projectId,
            String key
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message insert(
            String username,
            String projectId,
            String key
    ) throws MessageExistException, ProjectUnknownException, UserUnknownException, UserRightsException;

    Message update(
            String username,
            String projectId,
            String key,
            Locale locale,
            String translation
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Message update(
            String userId,
            String projectId,
            Message message
    ) throws MessageUnknownException, ProjectUnknownException, UserUnknownException, UserRightsException;

    Message copy(
            String username,
            String projectId,
            String sourceKey,
            String targetKey
    ) throws MessageExistException, MessageUnknownException, ProjectUnknownException, UserUnknownException, UserRightsException;

    void remove(
            String username,
            String projectId,
            String key
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Map<String, Map<Locale, String>> findForSpringMessageSource(
            String projectId
    ) throws ProjectUnknownException;

    Map<String, String> findForAngularMessageSource(
            String projectId,
            Locale locale
    ) throws ProjectUnknownException;
}
