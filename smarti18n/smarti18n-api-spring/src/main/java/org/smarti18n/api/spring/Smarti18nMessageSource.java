package org.smarti18n.api.spring;

import org.smarti18n.api.MessageTranslations;
import org.smarti18n.api.MessagesApi;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Smarti18nMessageSource extends AbstractMessageSource {

    private final Map<String, Map<Locale, String>> messages;
    private final MessagesApi messagesApi;

    public Smarti18nMessageSource(final MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
        this.messages = new HashMap<>();

        refreshMessageSource();
    }

    @Scheduled(cron = "0 * * * *")
    public void refreshMessageSource() {
        final Collection<MessageTranslations> messageTranslations = this.messagesApi.findAll();
        final Map<String, Map<Locale, String>> messages = new HashMap<>();

        for (MessageTranslations messageTranslation : messageTranslations) {
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
}
