package org.smarti18n.api.spring;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class Smarti18nMessageSource extends AbstractMessageSource {

    private static final String PATH_MESSAGES_SOURCE = "/api/1/messages/findForSpringMessageSource";

    private final Map<String, Map<Locale, String>> messages;

    public Smarti18nMessageSource(final String host, final String projectId, final String projectSecret) {
        this.messages = new HashMap<>();

        final UpdateMessageSourceTask task = new UpdateMessageSourceTask(
                host,
                projectId,
                projectSecret,
                messages
        );

        new Timer(true).schedule(task, 0, 60 * 1000);
    }

    @Override
    protected MessageFormat resolveCode(final String code, final Locale locale) {
        if (!this.messages.containsKey(code) || !this.messages.get(code).containsKey(locale)) {
            return new MessageFormat("?" + code + "?");
        }

        final String message = this.messages.get(code).get(new Locale(locale.getLanguage()));

        return new MessageFormat(message, locale);
    }

    private static class UpdateMessageSourceTask extends TimerTask {

        private final RestTemplate restTemplate;

        private final String host;
        private final String projectId;
        private final String projectSecret;
        private final Map<String, Map<Locale, String>> messages;

        private UpdateMessageSourceTask(
                final String host,
                final String projectId,
                final String projectSecret,
                final Map<String, Map<Locale, String>> messages) {

            this.restTemplate = new RestTemplate();

            this.host = host;
            this.projectId = projectId;
            this.projectSecret = projectSecret;
            this.messages = messages;
        }


        @Override
        public void run() {
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.host).path(PATH_MESSAGES_SOURCE)
                    .queryParam("projectId", projectId)
                    .queryParam("projectSecret", projectSecret);

            final Map<String, Map<Locale, String>> messages = this.restTemplate.exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers()),
                    new ParameterizedTypeReference<Map<String, Map<Locale, String>>>() {
                    }
            ).getBody();

            this.messages.putAll(messages);
        }

        private static HttpHeaders headers() {
            final String plainCredentials = "default:default";
            final String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));

            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + base64Credentials);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            return headers;
        }
    }

}
