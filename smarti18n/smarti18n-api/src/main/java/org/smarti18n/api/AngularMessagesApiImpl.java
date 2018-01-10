package org.smarti18n.api;

import java.util.Locale;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.smarti18n.models.UserCredentialsSupplier;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class AngularMessagesApiImpl extends AbstractApiImpl implements AngularMessagesApi {

    public AngularMessagesApiImpl(
            final RestTemplate restTemplate,
            final String host) {

        super(restTemplate, host, new UserCredentialsSupplier());
    }

    public AngularMessagesApiImpl(
            final RestTemplate restTemplate,
            final int port) {

        super(restTemplate, port, new UserCredentialsSupplier());
    }

    @Override
    public Map<String, String> getMessages(final String projectId, final Locale locale) {
        final UriComponentsBuilder uri = uri(PATH_MESSAGES_FIND_ANGULAR, projectId).queryParam("locale", locale);

        final ResponseEntity<Map<String, String>> exchange = this.restTemplate.exchange(
                uri.build().encode().toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, String>>() {
                }
        );
        return handleResponse(exchange);
    }
}
