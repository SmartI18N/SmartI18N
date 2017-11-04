package org.smarti18n.api.impl;

import org.smarti18n.api.MessageSimple;
import org.smarti18n.api.MessageTranslations;
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

    private final static String DEFAULT_HOST = "https://messages.smarti18n.com";

    private final String host;
    private final RestTemplate restTemplate;

    public MessagesApiImpl(final Environment environment, final RestTemplate restTemplate) {
        this.host = environment.getProperty("", DEFAULT_HOST);
        this.restTemplate = restTemplate;
    }

    @Override
    public String root() {
        return this.restTemplate.getForObject(host + PATH_ROOT, String.class);
    }

    @Override
    public MessageSimple findOne(final String key, final Locale language) {
        return this.restTemplate.getForObject(host + PATH_FIND_ONE + "?key=" + key + "&language=" + language, MessageSimple.class);
    }

    @Override
    public Collection<MessageTranslations> findAll() {
        final MessageTranslations[] translations = this.restTemplate.getForObject(host + PATH_FIND_ALL, MessageTranslations[].class);
        if (translations == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(
                translations
        );
    }

    @Override
    public MessageTranslations insert(final String key) {
        return this.restTemplate.getForObject(host + PATH_INSERT + "?key=" + key, MessageTranslations.class);
    }

    @Override
    public MessageTranslations save(final String key, final String translation, final Locale language) {
        return this.restTemplate.getForObject(host + PATH_SAVE + "?key=" + key + "&translation=" + translation + "&language=" + language, MessageTranslations.class);
    }

    @Override
    public MessageTranslations copy(final String sourceKey, final String targetKey) {
        return this.restTemplate.getForObject(host + PATH_COPY + "?sourceKey=" + sourceKey + "&targetKey=" + targetKey, MessageTranslations.class);
    }

    @Override
    public void remove(final String key) {
        this.restTemplate.getForObject(host + PATH_REMOVE + "?key=" + key, Void.class);
    }
}
