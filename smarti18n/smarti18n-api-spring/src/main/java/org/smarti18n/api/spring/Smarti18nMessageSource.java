package org.smarti18n.api.spring;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Smarti18nMessageSource extends AbstractMessageSource {

    private final Map<String, Map<Locale, String>> messages;
    private final RestTemplate restTemplate;

    private final String host;

    public Smarti18nMessageSource(final String host) {
        this.host = host;
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
        return this.restTemplate.getForObject("", Map.class);
    }

}
