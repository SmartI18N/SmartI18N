package org.smarti18n.api;

import java.util.Base64;
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
    private static final String DEFAULT_USERNAME = "default@smarti18n.com";
    private static final String DEFAULT_PASSWORD = "default";

    final RestTemplate restTemplate;

    private final String host;
    private final String username;
    private final String password;

    AbstractApiImpl(
            final RestTemplate restTemplate,
            final Environment environment) {

        this.restTemplate = restTemplate;
        this.host = environment.getProperty("smarti18n.messages.host", DEFAULT_HOST);
        this.username = environment.getProperty("smarti18n.messages.username", DEFAULT_USERNAME);
        this.password = environment.getProperty("smarti18n.messages.password", DEFAULT_PASSWORD);
    }

    AbstractApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final String username,
            final String password) {

        this.restTemplate = restTemplate;
        this.host = host;
        this.username = username;
        this.password = password;
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
        final String plainCredentials = username + ":" + password;
        final String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
