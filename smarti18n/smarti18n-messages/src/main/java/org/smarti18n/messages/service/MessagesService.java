package org.smarti18n.messages.service;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface MessagesService {

    Collection<Message> findAll(
            String username,
            String projectId
    );

    Message findOne(
            String username,
            String projectId,
            String key
    );

    Message insert(
            String username,
            String projectId,
            String key
    );

    Message update(
            String username,
            String projectId,
            String key,
            Locale locale,
            String translation
    );

    Message update(
            String userId,
            String projectId,
            Message message
    );

    Message copy(
            String username,
            String projectId,
            String sourceKey,
            String targetKey
    );

    void remove(
            String username,
            String projectId,
            String key
    );

    Map<String, Map<Locale, String>> findForSpringMessageSource(
            String projectId
    );

    Map<String, String> findForAngularMessageSource(
            String projectId,
            Locale locale
    );
}
