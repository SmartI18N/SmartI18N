package org.smarti18n.api.impl;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * @author Marc Bellmann &lt;marc@smarti18n.com&gt;
 */
@Service
public class MessagesApiImpl implements MessagesApi {

    public final static String DEFAULT_HOST = "https://messages.smarti18n.com";

    private final String host;
    private final RestTemplate restTemplate;

    public MessagesApiImpl(final Environment environment, final RestTemplate restTemplate) {
        this.host = environment.getProperty("", DEFAULT_HOST);
        this.restTemplate = restTemplate;
    }

    public MessagesApiImpl(final RestTemplate restTemplate, final int port) {
        this.host = "http://localhost:" + port;
        this.restTemplate = restTemplate;
    }

    @Override
    public Collection<MessageImpl> findAll() {
        final MessageImpl[] translations = this.restTemplate.getForObject(host + MessagesApi.PATH_MESSAGES_FIND_ALL, MessageImpl[].class);
        if (translations == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(
                translations
        );
    }

    @Override
    public MessageImpl insert(final String key) {
        return this.restTemplate.getForObject(host + MessagesApi.PATH_MESSAGES_INSERT + "?key=" + key, MessageImpl.class);
    }

    @Override
    public MessageImpl save(final String key, final String translation, final Locale language) {
        return this.restTemplate.getForObject(host + MessagesApi.PATH_MESSAGES_SAVE + "?key=" + key + "&translation=" + translation + "&language=" + language, MessageImpl.class);
    }

    @Override
    public MessageImpl copy(final String sourceKey, final String targetKey) {
        return this.restTemplate.getForObject(host + MessagesApi.PATH_MESSAGES_COPY + "?sourceKey=" + sourceKey + "&targetKey=" + targetKey, MessageImpl.class);
    }

    @Override
    public void remove(final String key) {
        this.restTemplate.getForObject(host + MessagesApi.PATH_MESSAGES_REMOVE + "?key=" + key, Void.class);
    }
}
