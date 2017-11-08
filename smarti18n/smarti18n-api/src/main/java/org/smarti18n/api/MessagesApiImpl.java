package org.smarti18n.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Marc Bellmann &lt;marc@smarti18n.com&gt;
 */
@Service
public class MessagesApiImpl extends AbstractApiImpl implements MessagesApi {

    public MessagesApiImpl(final Environment environment, final RestTemplate restTemplate) {
        super(restTemplate, environment.getProperty("", DEFAULT_HOST));
    }

    public MessagesApiImpl(final RestTemplate restTemplate, final int port) {
        super(restTemplate, "http://localhost:" + port);
    }

    @Override
    public Collection<MessageImpl> findAll(final String projectId, final String projectSecret) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_FIND_ALL, projectId, projectSecret);

        return Arrays.asList(
                get(uri, MessageImpl[].class)
        );
    }

    @Override
    public Map<String, Map<Locale, String>> findForSpringMessageSource(final String projectId, final String projectSecret) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_FIND_SPRING, projectId, projectSecret);

        return get(uri, Map.class);
    }

    @Override
    public MessageImpl insert(final String projectId, final String projectSecret, final String key) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_INSERT, projectId, projectSecret)
                .queryParam("key", key);

        return get(uri, MessageImpl.class);
    }

    @Override
    public MessageImpl update(final String projectId, final String projectSecret, final String key, final String translation, final Locale language) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_UPDATE, projectId, projectSecret)
                .queryParam("key", key)
                .queryParam("translation", translation)
                .queryParam("language", language);

        return get(uri, MessageImpl.class);
    }

    @Override
    public MessageImpl copy(final String projectId, final String projectSecret, final String sourceKey, final String targetKey) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_COPY, projectId, projectSecret)
                .queryParam("sourceKey", sourceKey)
                .queryParam("targetKey", targetKey);

        return get(uri, MessageImpl.class);
    }

    @Override
    public void remove(final String projectId, final String projectSecret, final String key) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_REMOVE, projectId, projectSecret)
                .queryParam("key", key);

        get(uri, Void.class);
    }

}
