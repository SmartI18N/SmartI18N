package org.smarti18n.messages.service;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.smarti18n.api.MessageImpl;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface MessagesService {

    Collection<MessageImpl> findAll(
            final String username, String projectId
    );

    MessageImpl findOne(
            final String username, String projectId,
            String key
    );

    MessageImpl insert(
            final String username, String projectId,
            String key
    );

    MessageImpl update(
            final String username, String projectId,
            String key,
            String translation,
            Locale language
    );

    MessageImpl copy(
            final String username, String projectId,
            String sourceKey,
            String targetKey
    );

    void remove(
            final String username, String projectId,
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
