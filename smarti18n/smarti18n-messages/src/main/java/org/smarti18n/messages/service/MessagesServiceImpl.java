package org.smarti18n.messages.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.api.Message;
import org.smarti18n.api.MessageImpl;
import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.MessageRepository;

import static org.smarti18n.messages.service.IdentifierUtils.clean;

@Service
public class MessagesServiceImpl implements MessagesService {

    private final Logger logger = LoggerFactory.getLogger(MessagesServiceImpl.class);

    private final MessageRepository messageRepository;
    private final EntityLoader entityLoader;


    public MessagesServiceImpl(
            final MessageRepository messageRepository,
            final EntityLoader entityLoader) {

        this.messageRepository = messageRepository;
        this.entityLoader = entityLoader;
    }

    @Override
    @Transactional
    public Collection<Message> findAll(final String username, final String projectId) {

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        return findByIdProject(project).stream().map(messageEntity -> new MessageImpl(
                messageEntity.getKey(),
                messageEntity.getTranslations()
        ))
                .sorted(Comparator.comparing(MessageImpl::getKey))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Message findOne(final String username, final String projectId, final String key) {
        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        final Optional<MessageEntity> messageEntity = this.messageRepository.findById(
                new MessageEntity.MessageId(clean(key), project)
        );

        if (messageEntity.isPresent()) {
            final MessageImpl message = new MessageImpl(messageEntity.get());
            project.getLocales().forEach(locale -> message.getTranslations().putIfAbsent(locale, ""));
            return message;
        }

        return null;
    }

    @Override
    @Transactional
    public Message insert(
            final String username,
            final String projectId,
            final String key) {

        final String cleanKey = clean(key.trim());

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        if (this.messageRepository.findById(new MessageEntity.MessageId(cleanKey, project)).isPresent()) {
            throw new IllegalStateException("Message with key [" + cleanKey + "] already exist.");
        }

        return new MessageImpl(
                this.messageRepository.insert(new MessageEntity(cleanKey, project))
        );
    }

    @Override
    @Transactional
    public Message update(
            final String username, final String projectId,
            final String key,
            final Locale locale, final String translation) {

        final String cleanKey = clean(key);

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        final Optional<MessageEntity> optional = this.messageRepository.findById(
                new MessageEntity.MessageId(cleanKey, project)
        );
        final MessageEntity messageEntity = optional.orElseGet(() -> new MessageEntity(cleanKey, project));

        messageEntity.putTranslation(locale, translation);

        final MessageEntity saved = this.messageRepository.save(messageEntity);

        return new MessageImpl(
                saved.getKey(),
                saved.getTranslations()
        );
    }

    @Override
    @Transactional
    public Message update(final String username, final String projectId, final Message message) {
        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        final Optional<MessageEntity> optional = this.messageRepository.findById(
                new MessageEntity.MessageId(message.getKey(), project)
        );

        final MessageEntity messageEntity = optional.orElseThrow(() -> new IllegalStateException(
                "Message with key [" + message.getKey() + "] and Project [" + projectId + "] doesn't exist!"
        ));

        messageEntity.setTranslations(message.getTranslations());

        return this.messageRepository.save(messageEntity);
    }

    @Override
    @Transactional
    public Message copy(
            final String username, final String projectId,
            final String sourceKey,
            final String targetKey) {

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        final Optional<MessageEntity> optional = this.messageRepository.findById(new MessageEntity.MessageId(clean(sourceKey), project));

        if (!optional.isPresent()) {
            throw new IllegalStateException("Message with key [" + clean(sourceKey) + "] doesn't exist.");
        }

        final String cleanedTargetKey = clean(targetKey);

        if (this.messageRepository.findById(new MessageEntity.MessageId(cleanedTargetKey, project)).isPresent()) {
            throw new IllegalStateException("Message with key [" + cleanedTargetKey + "] already exist.");
        }

        final MessageEntity messageEntity = optional.get();
        messageEntity.setKey(cleanedTargetKey);

        final MessageEntity saved = this.messageRepository.save(messageEntity);

        return new MessageImpl(
                saved.getKey(),
                saved.getTranslations()
        );

    }

    @Override
    @Transactional
    public void remove(
            final String username, final String projectId,
            final String key) {

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        this.messageRepository.deleteById(new MessageEntity.MessageId(clean(key), project));
    }

    @Override
    @Transactional
    public Map<String, Map<Locale, String>> findForSpringMessageSource(final String projectId) {
        final ProjectEntity project = this.entityLoader.findProject(projectId);

        final Collection<MessageEntity> messages = findByIdProject(project);
        final Map<String, Map<Locale, String>> map = new HashMap<>();

        for (final MessageEntity message : messages) {
            map.put(message.getKey(), new HashMap<>(message.getTranslations()));
        }

        return map;
    }

    @Override
    @Transactional
    public Map<String, String> findForAngularMessageSource(final String projectId, final Locale locale) {
        final ProjectEntity project = this.entityLoader.findProject(projectId);

        final Collection<MessageEntity> messages = findByIdProject(project);

        final Map<String, String> map = new HashMap<>();

        for (final MessageEntity message : messages) {
            if (message.getTranslations().containsKey(locale)) {
                map.put(message.getKey(), message.getTranslation(locale));
            }
            final Locale languageLocale = new Locale(locale.getLanguage());
            if (message.getTranslations().containsKey(languageLocale)) {
                map.put(message.getKey(), message.getTranslation(languageLocale));
            }
        }

        return map;
    }

    private Collection<MessageEntity> findByIdProject(final ProjectEntity project) {
        final Long startTime = System.currentTimeMillis();

        final Collection<MessageEntity> messages = this.messageRepository.findByIdProject(project);

        final Long endTime = System.currentTimeMillis();

        this.logger.info("find messages by project in " + (endTime - startTime) + "ms");

        return messages;
    }
}