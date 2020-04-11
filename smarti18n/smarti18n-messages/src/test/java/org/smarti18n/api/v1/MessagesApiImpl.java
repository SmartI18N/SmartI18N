package org.smarti18n.api.v1;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.smarti18n.api.AbstractApiImpl;
import org.smarti18n.api.v1.MessagesApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.smarti18n.models.Message;
import org.smarti18n.models.MessageImpl;
import org.smarti18n.models.UserCredentialsSupplier;

/**
 * @author Marc Bellmann &lt;marc@smarti18n.com&gt;
 */
public class MessagesApiImpl extends AbstractApiImpl implements MessagesApi {

    public MessagesApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, host, userCredentialsSupplier);
    }

    public MessagesApiImpl(
            final RestTemplate restTemplate,
            final int port,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, port, userCredentialsSupplier);
    }

    @Override
    public Collection<Message> findAll(final String projectId) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_FIND_ALL, projectId);

        return Arrays.asList(
                get(uri, MessageImpl[].class)
        );
    }

    @Override
    public Message findOne(final String projectId, final String key) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_FIND_ONE, projectId)
                .queryParam("key", key);

        return get(uri, MessageImpl.class);
    }

    @Override
    public Message insert(final String projectId, final String key) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_INSERT, projectId)
                .queryParam("key", key);

        return get(uri, MessageImpl.class);
    }

    @Override
    public Message update(final String projectId, final String key, final Locale locale, final String translation) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_UPDATE, projectId)
                .queryParam("key", key)
                .queryParam("translation", translation)
                .queryParam("locale", locale);

        return get(uri, Message.class);
    }

    @Override
    public Message update(final String projectId, final Message message) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_UPDATE, projectId);

        return post(uri, message, Message.class);
    }

    @Override
    public Message copy(final String projectId, final String sourceKey, final String targetKey) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_COPY, projectId)
                .queryParam("sourceKey", sourceKey)
                .queryParam("targetKey", targetKey);

        return get(uri, MessageImpl.class);
    }

    @Override
    public void remove(final String projectId, final String key) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_REMOVE, projectId)
                .queryParam("key", key);

        get(uri, Void.class);
    }

}
