package org.smarti18n.api;

import org.smarti18n.exceptions.UnexpectedApiException;
import org.smarti18n.models.UserCredentialsSupplier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public abstract class AbstractApiImpl {

    final RestTemplate restTemplate;

    private final String host;
    private final UserCredentialsSupplier userCredentialsSupplier;

    public AbstractApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final UserCredentialsSupplier userCredentialsSupplier) {

        this.restTemplate = restTemplate;
        this.host = host;
        this.userCredentialsSupplier = userCredentialsSupplier;
    }

    public AbstractApiImpl(
            final RestTemplate restTemplate,
            final int port,
            final UserCredentialsSupplier userCredentialsSupplier) {

        this.restTemplate = restTemplate;
        this.host = "http://localhost:" + port;
        this.userCredentialsSupplier = userCredentialsSupplier;
    }

    public <OUT> OUT get(final UriComponentsBuilder uri, Class<OUT> responseType) {
        return handleResponse(
                send(HttpMethod.GET, uri, null, responseType)
        );
    }

    public <OUT, IN> OUT post(final UriComponentsBuilder uri, final IN dto, final Class<OUT> responseType) {
        return handleResponse(
                send(HttpMethod.POST, uri, dto, responseType)
        );
    }

    public <OUT, IN> OUT put(final UriComponentsBuilder uri, final IN dto, final Class<OUT> responseType) {
        return handleResponse(
                send(HttpMethod.PUT, uri, dto, responseType)
        );
    }

    public <OUT> OUT delete(final UriComponentsBuilder uri, Class<OUT> responseType) {
        return handleResponse(
                send(HttpMethod.DELETE, uri, null, responseType)
        );
    }

    private <OUT> ResponseEntity<OUT> send(HttpMethod method, UriComponentsBuilder uri, Object body, Class<OUT> responseType) {
        return this.restTemplate.exchange(
                uri.build().encode().toUri(),
                method,
                new HttpEntity<>(body, headers()),
                responseType
        );
    }

    public UriComponentsBuilder uri(final String path) {
        return UriComponentsBuilder.fromHttpUrl(this.host)
                .path(path);
    }

    @Deprecated
    public UriComponentsBuilder uri(final String path, final String projectId) {
        return uri(path)
                .queryParam("projectId", projectId);
    }

    public <OUT> OUT handleResponse(final ResponseEntity<OUT> exchange) {
        if (exchange.getStatusCode().isError()) {
            throw new UnexpectedApiException("SmartI18N Message API: " + exchange.getStatusCode().getReasonPhrase());
        }

        return exchange.getBody();
    }

    private HttpHeaders headers() {
        final String base64Credentials = this.userCredentialsSupplier.getBase64Credentials();

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
