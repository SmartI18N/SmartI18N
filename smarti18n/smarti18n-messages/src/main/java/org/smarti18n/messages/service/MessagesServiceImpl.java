package org.smarti18n.messages.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.MessageRepository;
import org.smarti18n.messages.repositories.ProjectRepository;

@Service
public class MessagesServiceImpl implements MessagesService {

    private final MessageRepository messageRepository;
    private final ProjectRepository projectRepository;

    public MessagesServiceImpl(final MessageRepository messageRepository, final ProjectRepository projectRepository) {
        this.messageRepository = messageRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public Collection<MessageImpl> findAll(
            final String projectId,
            final String projectSecret) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        return this.messageRepository.findByIdProject(project).stream().map(messageEntity -> new MessageImpl(
                messageEntity.getKey(),
                messageEntity.getTranslations()
        )).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Map<Locale, String>> findForSpringMessageSource(
            final String projectId,
            final String projectSecret) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        final Collection<MessageEntity> messages = this.messageRepository.findByIdProject(project);
        final Map<String, Map<Locale, String>> map = new HashMap<>();

        for (final MessageEntity message : messages) {
            map.put(message.getKey(), new HashMap<>(message.getTranslations()));
        }

        return map;
    }

    @Override
    @Transactional
    public MessageImpl insert(
            final String projectId,
            final String projectSecret,
            final String key) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        if (this.messageRepository.findById(new MessageEntity.MessageId(key, project)).isPresent()) {
            throw new IllegalStateException("Message with key [" + key + "] already exist.");
        }

        return new MessageImpl(
                this.messageRepository.insert(new MessageEntity(key, project))
        );
    }

    @Override
    @Transactional
    public MessageImpl update(
            final String projectId,
            final String projectSecret,
            final String key,
            final String translation,
            final Locale language) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        final Optional<MessageEntity> optional = this.messageRepository.findById(new MessageEntity.MessageId(key, project));
        final MessageEntity messageEntity = optional.orElseGet(() -> new MessageEntity(key, project));

        messageEntity.putTranslation(language, translation);

        final MessageEntity saved = this.messageRepository.save(messageEntity);

        return new MessageImpl(
                saved.getKey(),
                saved.getTranslations()
        );
    }

    @Override
    @Transactional
    public MessageImpl copy(
            final String projectId,
            final String projectSecret,
            final String sourceKey,
            final String targetKey) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        final Optional<MessageEntity> optional = this.messageRepository.findById(new MessageEntity.MessageId(sourceKey, project));

        if (!optional.isPresent()) {
            throw new IllegalStateException("Message with key [" + sourceKey + "] doesn't exist.");
        }

        if (this.messageRepository.findById(new MessageEntity.MessageId(targetKey, project)).isPresent()) {
            throw new IllegalStateException("Message with key [" + targetKey + "] already exist.");
        }

        final MessageEntity messageEntity = optional.get();
        messageEntity.setKey(targetKey);

        final MessageEntity saved = this.messageRepository.save(messageEntity);

        return new MessageImpl(
                saved.getKey(),
                saved.getTranslations()
        );

    }

    @Override
    @Transactional
    public void remove(
            final String projectId,
            final String projectSecret,
            final String key) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        this.messageRepository.deleteById(new MessageEntity.MessageId(key, project));
    }

    private ProjectEntity validateAndGetProject(final String projectId, final String projectSecret) {
        Assert.notNull(projectId, "projectId");
        Assert.notNull(projectSecret, "projectSecret");

        final Optional<ProjectEntity> optional = this.projectRepository.findById(projectId);
        if (optional.isPresent()) {
            final ProjectEntity projectEntity = optional.get();

            if (projectEntity.containsSecret(projectSecret)) {
                return projectEntity;
            }
        }

        throw new IllegalStateException("Project with ID [" + projectId + "] doesn't exist.");
    }
}