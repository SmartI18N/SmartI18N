package org.smarti18n.api.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;

/**
 * @author Marc Bellmann &lt;marc@smarti18n.com&gt;
 */
@Service
public class MessagesApiImpl extends AbstractApiImpl implements MessagesApi {

    public MessagesApiImpl(final Environment environment, final RestTemplate restTemplate) {
        super(
                environment.getProperty("", DEFAULT_HOST),
                restTemplate,
                environment.getProperty("", DEFAULT_PROJECT_ID),
                environment.getProperty("", DEFAULT_PROJECT_ID));
    }

    public MessagesApiImpl(final RestTemplate restTemplate, final int port, final String projectSecret) {
        super("http://localhost:" + port, restTemplate, "test", projectSecret);
    }

    @Override
    public Collection<MessageImpl> findAll() {
        return Arrays.asList(
                get(MessagesApi.PATH_MESSAGES_FIND_ALL, MessageImpl[].class)
        );
    }

    @Override
    public MessageImpl insert(final String key) {

        return get(MessagesApi.PATH_MESSAGES_INSERT + "?key=" + key, MessageImpl.class);
    }

    @Override
    public MessageImpl update(final String key, final String translation, final Locale language) {
        return get(MessagesApi.PATH_MESSAGES_UPDATE + "?key=" + key + "&translation=" + translation + "&language=" + language, MessageImpl.class);
    }

    @Override
    public MessageImpl copy(final String sourceKey, final String targetKey) {
        return get(MessagesApi.PATH_MESSAGES_COPY + "?sourceKey=" + sourceKey + "&targetKey=" + targetKey, MessageImpl.class);
    }

    @Override
    public void remove(final String key) {
        get(MessagesApi.PATH_MESSAGES_REMOVE + "?key=" + key, Void.class);
    }

}
