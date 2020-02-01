package org.smarti18n.messages.messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.smarti18n.messages.projects.ProjectEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import org.smarti18n.models.Message;

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

    public void putTranslation(final Locale locale, final String translation) {
        this.translations.put(locale, translation);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageEntity)) {
            return false;
        }

        final MessageEntity that = (MessageEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return this.id.key + " " + this.id.project.toString();
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

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MessageId)) {
                return false;
            }

            final MessageId messageId = (MessageId) o;

            if (!key.equals(messageId.key)) {
                return false;
            }
            return project.equals(messageId.project);
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + project.hashCode();
            return result;
        }
    }
}
