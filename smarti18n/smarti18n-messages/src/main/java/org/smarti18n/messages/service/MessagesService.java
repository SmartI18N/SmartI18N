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
            String projectId
    );

    MessageImpl findOne(
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
            String translation,
            Locale language
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

    Map<String, Map<Locale, String>> findForSpringMessageSource(
            String projectId
    );

    Map<String, String> findForAngularMessageSource(
            String projectId,
            Locale locale
    );
}
