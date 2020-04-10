package org.smarti18n.api.spring;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class Smarti18nMessageSource extends AbstractMessageSource {

    /**
     * Path to load all Messages
     */
    private static final String PATH_MESSAGES_SOURCE = "/api/2/projects/PROJECT_ID/messages";

    /**
     * Message Cache
     */
    private final Map<String, Map<Locale, String>> messages;

    /**
     * Default Constructor for HelloWorld Example
     */
    public Smarti18nMessageSource() {
        this("default", "default");
    }

    /**
     * smarti18n.com Constructor
     *
     * @param projectId     Project ID
     * @param projectSecret Project Secret from Project Settings
     */
    public Smarti18nMessageSource(final String projectId, final String projectSecret) {
        this("https://messages.smarti18n.com", projectId, projectSecret);
    }

    /**
     * Own hosted smarti18n Construktor
     *
     * @param host          URL to own hosted smarti18n
     * @param projectId     Project ID
     * @param projectSecret Project Secret from Project Settings
     */
    public Smarti18nMessageSource(final String host, final String projectId, final String projectSecret) {
        Assert.notNull(host, "host");
        Assert.notNull(projectId, "projectId");
        Assert.notNull(projectSecret, "projectSecret");

        this.messages = new HashMap<>();

        final UpdateMessageSourceTask task = new UpdateMessageSourceTask(
                host,
                projectId,
                projectSecret,
                this.messages
        );

        // Update Every Minute
        new Timer(true).schedule(task, 0, 60 * 1000);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MessageFormat resolveCode(final String code, final Locale locale) {
        if (this.messages.containsKey(code)) {
            final Map<Locale, String> translations = this.messages.get(code);

            if (translations.containsKey(locale)) {
                return new MessageFormat(translations.get(locale), locale);
            }

            final Locale languageLocale = new Locale(locale.getLanguage());
            if (translations.containsKey(languageLocale)) {
                return new MessageFormat(translations.get(languageLocale), languageLocale);
            }
        }

        return new MessageFormat("?" + code + "?");
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
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.host)
                    .path(PATH_MESSAGES_SOURCE.replaceAll("PROJECT_ID", projectId));

            final List<Smarti18nMessage> result = this.restTemplate.exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers()),
                    new ParameterizedTypeReference<List<Smarti18nMessage>>() {
                    }
            ).getBody();

            if (result != null) {
                for (Smarti18nMessage message : result) {
                    this.messages.put(message.getKey(), message.getTranslations());
                }
            }
        }

        private HttpHeaders headers() {
            final String plainCredentials = this.projectId + ":" + this.projectSecret;
            final String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));

            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + base64Credentials);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            return headers;
        }
    }

    private static class Smarti18nMessage {
        private String key;
        private Map<Locale, String> translations;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Map<Locale, String> getTranslations() {
            return translations;
        }

        public void setTranslations(Map<Locale, String> translations) {
            this.translations = translations;
        }
    }
}
