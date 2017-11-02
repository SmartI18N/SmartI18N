package org.smarti18n.editor.endpoints;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.springframework.web.client.RestTemplate;

import org.smarti18n.api.MessageSimple;
import org.smarti18n.api.MessageTranslations;
import org.smarti18n.api.MessagesApi;

/**
 * @author Marc Bellmann &lt;marc@smarti18n.com&gt;
 */
public class MessagesApiImpl implements MessagesApi {

    private static final String HOST = "https://messages.smarti18n.com";

    private final RestTemplate restTemplate;

    public MessagesApiImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String root() {
        return this.restTemplate.getForObject(HOST + "/api/1/root", String.class);
    }

    @Override
    public MessageSimple findOne(final String key, final Locale language) {
        return this.restTemplate.getForObject(HOST + "/api/1/findOne?key=" + key + "&language=" + language, MessageSimple.class);
    }

    @Override
    public Collection<MessageTranslations> findAll() {
        return Arrays.asList(
                this.restTemplate.getForObject(HOST + "/api/1/findAll", MessageTranslations[].class)
        );
    }

    @Override
    public MessageTranslations insert(final String key) {
        return this.restTemplate.getForObject(HOST + "/api/1/insert?key=" + key, MessageTranslations.class);
    }

    @Override
    public MessageTranslations save(final String key, final String translation, final Locale language) {
        return this.restTemplate.getForObject(HOST + "/api/1/save?key=" + key + "&translation=" + translation + "&language=" + language, MessageTranslations.class);
    }
}
