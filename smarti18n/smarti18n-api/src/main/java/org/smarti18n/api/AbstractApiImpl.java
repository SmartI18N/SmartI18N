package org.smarti18n.api;

import java.util.Collections;

import org.springframework.core.env.Environment;
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
abstract class AbstractApiImpl {

    public static final String DEFAULT_HOST = "https://messages.smarti18n.com";

    final RestTemplate restTemplate;

    private final String host;
    private final UserCredentialsSupplier userCredentialsSupplier;

    AbstractApiImpl(
            final RestTemplate restTemplate,
            final Environment environment,
            final UserCredentialsSupplier userCredentialsSupplier) {

        this.restTemplate = restTemplate;
        this.host = environment.getProperty("smarti18n.messages.host", DEFAULT_HOST);
        this.userCredentialsSupplier = userCredentialsSupplier;
    }

    AbstractApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final UserCredentialsSupplier userCredentialsSupplier) {

        this.restTemplate = restTemplate;
        this.host = host;
        this.userCredentialsSupplier = userCredentialsSupplier;
    }

    <OUT, IN> OUT post(final UriComponentsBuilder uri, final IN project, final Class<OUT> responseType) {
        final ResponseEntity<OUT> exchange = this.restTemplate.exchange(
                uri.build().encode().toUri(),
                HttpMethod.POST,
                new HttpEntity<>(project, headers()),
                responseType
        );

        return handleResponse(exchange);
    }

    <OUT> OUT get(final UriComponentsBuilder uri, Class<OUT> responseType) {
        final ResponseEntity<OUT> exchange = this.restTemplate.exchange(
                uri.build().encode().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers()),
                responseType
        );

        return handleResponse(exchange);
    }

    UriComponentsBuilder uri(final String path) {
        return UriComponentsBuilder.fromHttpUrl(this.host)
                .path(path);
    }

    UriComponentsBuilder uri(final String path, final String projectId) {
        return uri(path)
                .queryParam("projectId", projectId);
    }

    <OUT> OUT handleResponse(final ResponseEntity<OUT> exchange) {
        if (exchange.getStatusCode().isError()) {
            throw new ApiException("SmartI18N Message API: " + exchange.getStatusCode().getReasonPhrase());
        }

        return exchange.getBody();
    }

    private HttpHeaders headers() {
        final String base64Credentials = this.userCredentialsSupplier.get().getBase64Credentials();

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
