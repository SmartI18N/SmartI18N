package org.smarti18n.messages.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import org.smarti18n.api.Message;

@Document(collection = "messages")
public class MessageEntity implements Message {

    @Id
    private MessageId id;

    private Map<Locale, String> translations;

    MessageEntity() {
        this.id = new MessageId();
    }

    public MessageEntity(final String key, final ProjectEntity project) {
        this.id = new MessageId(key, project);
        this.translations = new HashMap<>();
    }

    @Override
    public String getKey() {
        return this.id.key;
    }

    @Override
    public void setKey(final String key) {
        this.id.key = key;
    }

    @Override
    public Map<Locale, String> getTranslations() {
        return translations;
    }

    @Override
    public void setTranslations(final Map<Locale, String> translations) {
        this.translations = translations;
    }

    public void putTranslation(final Locale language, final String translation) {
        this.translations.put(language, translation);
    }

    public static class MessageId implements Serializable {

        private String key;

        @DBRef
        private ProjectEntity project;

        MessageId() {
        }

        public MessageId(final String key, final ProjectEntity project) {
            this.key = key;
            this.project = project;
        }

        String getKey() {
            return key;
        }

        ProjectEntity getProject() {
            return project;
        }
    }
}
