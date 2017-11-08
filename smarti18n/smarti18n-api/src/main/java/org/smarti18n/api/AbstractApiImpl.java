package org.smarti18n.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
abstract class AbstractApiImpl {

    public static final String DEFAULT_HOST = "https://messages.smarti18n.com";

    private final String host;
    private final RestTemplate restTemplate;

    AbstractApiImpl(
            final RestTemplate restTemplate,
            final String host) {

        this.host = host;
        this.restTemplate = restTemplate;
    }

    <OUT, IN> OUT post(final UriComponentsBuilder uri, final IN project, final Class<OUT> responseType) {
        final ResponseEntity<OUT> exchange = this.restTemplate.exchange(
                uri.build().encode().toUri(),
                HttpMethod.POST,
                new HttpEntity<>(project),
                responseType
        );

        return handleResponse(exchange);
    }

    <OUT> OUT get(final UriComponentsBuilder uri, Class<OUT> responseType) {
        final ResponseEntity<OUT> exchange = this.restTemplate.exchange(
                uri.build().encode().toUri(),
                HttpMethod.GET,
                null,
                responseType
        );

        return handleResponse(exchange);
    }

    UriComponentsBuilder uri(final String path) {
        return UriComponentsBuilder.fromHttpUrl(this.host)
                .path(path);
    }

    UriComponentsBuilder uri(final String path, final String projectId, final String projectSecret) {
        return uri(path)
                .queryParam("projectId", projectId)
                .queryParam("projectSecret", projectSecret);
    }

    private <OUT> OUT handleResponse(final ResponseEntity<OUT> exchange) {
        if (exchange.getStatusCode().isError()) {
            throw new ApiException("SmartI18N Message API: " + exchange.getStatusCode().getReasonPhrase());
        }

        return exchange.getBody();
    }
}
