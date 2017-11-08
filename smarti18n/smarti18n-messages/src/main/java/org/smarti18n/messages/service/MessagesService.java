package org.smarti18n.messages.service;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.smarti18n.api.MessageImpl;

/**
 * @author Marc Bellmann &lt;marc.bellmann@saxess.ag&gt;
 */
public interface MessagesService {

    Collection<MessageImpl> findAll(
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
