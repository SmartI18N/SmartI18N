package org.smarti18n.api.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public abstract class AbstractApiImpl {

    public final static String DEFAULT_HOST = "https://messages.smarti18n.com";

    private final String host;
    private final RestTemplate restTemplate;

    AbstractApiImpl(final String host, final RestTemplate restTemplate) {
        this.host = host;
        this.restTemplate = restTemplate;
    }


    <OUT, IN> OUT post(final String path, final IN project, final Class<OUT> responseType) {
        final ResponseEntity<OUT> exchange = this.restTemplate.exchange(
                this.host + path,
                HttpMethod.POST,
                new HttpEntity<IN>(project),
                responseType
        );

        return handleResponse(exchange);
    }

    <OUT> OUT get(final String path, Class<OUT> responseType) {
        final ResponseEntity<OUT> exchange = this.restTemplate.exchange(
                this.host + path,
                HttpMethod.GET,
                null,
                responseType
        );

        return handleResponse(exchange);
    }

    private <OUT> OUT handleResponse(final ResponseEntity<OUT> exchange) {
        if (exchange.getStatusCode().isError()) {
            throw new ApiException("SmartI18N Message API: " + exchange.getStatusCode().getReasonPhrase());
        }

        return exchange.getBody();
    }

}
