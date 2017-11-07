package org.smarti18n.api.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.smarti18n.api.MessagesApi;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public abstract class AbstractApiImpl {

    public static final String DEFAULT_HOST = "https://messages.smarti18n.com";
    static final String DEFAULT_PROJECT_ID = "default";

    private final String host;
    private final RestTemplate restTemplate;
    private final String projectId;
    private final String projectSecret;

    AbstractApiImpl(
            final String host,
            final RestTemplate restTemplate,
            final String projectId,
            final String projectSecret) {

        this.host = host;
        this.restTemplate = restTemplate;
        this.projectId = projectId;
        this.projectSecret = projectSecret;
    }


    <OUT, IN> OUT post(final String path, final IN project, final Class<OUT> responseType) {
        final ResponseEntity<OUT> exchange = this.restTemplate.exchange(
                this.host + path,
                HttpMethod.POST,
                new HttpEntity<>(project, getHttpHeaders()),
                responseType
        );

        return handleResponse(exchange);
    }

    <OUT> OUT get(final String path, Class<OUT> responseType) {

        final ResponseEntity<OUT> exchange = this.restTemplate.exchange(
                this.host + path,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                responseType
        );

        return handleResponse(exchange);
    }

    private HttpHeaders getHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(MessagesApi.PROJECT_ID_HEADER, projectId);
        headers.add(MessagesApi.PROJECT_SECRET_HEADER, projectSecret);
        return headers;
    }

    private <OUT> OUT handleResponse(final ResponseEntity<OUT> exchange) {
        if (exchange.getStatusCode().isError()) {
            throw new ApiException("SmartI18N Message API: " + exchange.getStatusCode().getReasonPhrase());
        }

        return exchange.getBody();
    }
}
