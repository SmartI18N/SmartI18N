package org.smarti18n.api;

import java.util.Base64;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class SpringMessagesApiImpl extends AbstractApiImpl implements SpringMessagesApi {

    private final String projectId;
    private final String projectSecret;

    SpringMessagesApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final String projectId,
            final String projectSecret) {

        super(restTemplate, host, () -> {
            throw new UnsupportedOperationException();
        });

        this.projectId = projectId;
        this.projectSecret = projectSecret;
    }

    public SpringMessagesApiImpl(
            final RestTemplate restTemplate,
            final int port,
            final String projectId,
            final String projectSecret) {

        super(restTemplate, port, () -> {
            throw new UnsupportedOperationException();
        });

        this.projectId = projectId;
        this.projectSecret = projectSecret;
    }

    @Override
    public Map<String, Map<Locale, String>> findForSpringMessageSource() {
        final UriComponentsBuilder builder = uri(PATH_MESSAGES_FIND_SPRING);

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

}