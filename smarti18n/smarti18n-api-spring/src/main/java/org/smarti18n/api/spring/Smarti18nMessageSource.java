package org.smarti18n.api.spring;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
        final Collection<MessageImpl> messageTranslations = findAll();
        final Map<String, Map<Locale, String>> messages = new HashMap<>();

        for (MessageImpl messageTranslation : messageTranslations) {
            messages.put(messageTranslation.getKey(), messageTranslation.getTranslations());
        }

        this.messages.putAll(messages);
    }

    @Override
    protected MessageFormat resolveCode(final String code, final Locale locale) {
        if (!this.messages.containsKey(code) || !this.messages.get(code).containsKey(locale)) {
            return new MessageFormat("?" + code + "?");
        }

        final String message = this.messages.get(code).get(locale);

        return new MessageFormat(message, locale);
    }

    private Collection<MessageImpl> findAll() {
        final MessageImpl[] translations = this.restTemplate.getForObject(host + MessagesApi.PATH_MESSAGES_FIND_ALL, MessageImpl[].class);
        if (translations == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(
                translations
        );
    }

}
