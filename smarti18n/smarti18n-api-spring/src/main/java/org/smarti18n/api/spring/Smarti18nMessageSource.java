package org.smarti18n.api.spring;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class Smarti18nMessageSource extends AbstractMessageSource {

    private static final String PATH_MESSAGES_SOURCE = "/api/1/messages/findForSpringMessageSource";

    private final Map<String, Map<Locale, String>> messages;
    private final RestTemplate restTemplate;

    private final String host;

    public Smarti18nMessageSource(final String host) {
        this.host = "https://messages.smarti18n.com";
        this.restTemplate = new RestTemplate();
        this.messages = new HashMap<>();

        refreshMessageSource();
    }

    @Scheduled(cron = "0 * * * *")
    public void refreshMessageSource() {
        this.messages.putAll(loadForSpringMessageSource());
    }

    @Override
    protected MessageFormat resolveCode(final String code, final Locale locale) {
        if (!this.messages.containsKey(code) || !this.messages.get(code).containsKey(locale)) {
            return new MessageFormat("?" + code + "?");
        }

        final String message = this.messages.get(code).get(locale);

        return new MessageFormat(message, locale);
    }

    private Map<String, Map<Locale, String>> loadForSpringMessageSource() {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.host).path(PATH_MESSAGES_SOURCE)
                .queryParam("projectId", "default").queryParam("projectSecret", "default");

        return this.restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Map<Locale, String>>>() {
                }
        ).getBody();
    }

}
