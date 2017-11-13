package org.smarti18n.api;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Marc Bellmann &lt;marc@smarti18n.com&gt;
 */
@Service
public class MessagesApiImpl extends AbstractApiImpl implements MessagesApi {

    private final String projectId;
    private final String projectSecret;

    public MessagesApiImpl(
            final RestTemplate restTemplate,
            final Environment environment,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, environment, userCredentialsSupplier);

        this.projectId = "default";
        this.projectSecret = "default";
    }

    public MessagesApiImpl(
            final RestTemplate restTemplate,
            final int port,
            final UserCredentialsSupplier userCredentialsSupplier,
            final String projectSecret) {

        super(restTemplate, "http://localhost:" + port, userCredentialsSupplier);
        this.projectId = "test";
        this.projectSecret = projectSecret;
    }

    @Override
    public Collection<MessageImpl> findAll(final String projectId) {
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
    public Map<String, Map<Locale, String>> findForSpringMessageSource() {
        final UriComponentsBuilder builder = uri(MessagesApi.PATH_MESSAGES_FIND_SPRING);

        final String plainCredentials = this.projectId + ":" + this.projectSecret;
        final String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        final ResponseEntity<Map<String, Map<Locale, String>>> exchange = this.restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<Map<String, Map<Locale, String>>>() {
                }
        );
        return handleResponse(exchange);
    }

    @Override
    public MessageImpl insert(final String projectId, final String key) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_INSERT, projectId)
                .queryParam("key", key);

        return get(uri, MessageImpl.class);
    }

    @Override
    public MessageImpl update(final String projectId, final String key, final String translation, final Locale language) {
        final UriComponentsBuilder uri = uri(MessagesApi.PATH_MESSAGES_UPDATE, projectId)
                .queryParam("key", key)
                .queryParam("translation", translation)
                .queryParam("language", language);

        return get(uri, MessageImpl.class);
    }

    @Override
    public MessageImpl copy(final String projectId, final String sourceKey, final String targetKey) {
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
