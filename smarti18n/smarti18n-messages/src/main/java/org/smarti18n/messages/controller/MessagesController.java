package org.smarti18n.messages.controller;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.MessageRepository;
import org.smarti18n.messages.repositories.ProjectRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MessagesController implements MessagesApi {

    private final MessageRepository messageRepository;
    private final ProjectRepository projectRepository;

    public MessagesController(
            final MessageRepository messageRepository,
            final ProjectRepository projectRepository) {

        this.messageRepository = messageRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ALL)
    public Collection<MessageImpl> findAll(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        return this.messageRepository.findByIdProject(project).stream().map(messageEntity -> new MessageImpl(
                messageEntity.getKey(),
                messageEntity.getTranslations()
        )).collect(Collectors.toList());
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_SPRING)
    public Map<String, Map<Locale, String>> findForSpringMessageSource(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        final Collection<MessageEntity> messages = this.messageRepository.findByIdProject(project);
        final Map<String, Map<Locale, String>> map = new HashMap<>();

        for (final MessageEntity message : messages) {
            map.put(message.getKey(), new HashMap<>(message.getTranslations()));
        }

        return map;
    }

    @Override
    @GetMapping(PATH_MESSAGES_INSERT)
    public MessageImpl insert(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret,
            @RequestParam("key") final String key) {

        final ProjectEntity project = validateAndGetProject(projectId, projectSecret);

        if (this.messageRepository.findById(new MessageEntity.MessageId(key, project)).isPresent()) {
            throw new IllegalStateException("Message with key [" + key + "] already exist.");
        }

        return new MessageImpl(
                this.messageRepository.insert(new MessageEntity(key, project))
        );
    }

    @Override
    @GetMapping(PATH_MESSAGES_UPDATE)
    public MessageImpl update(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret,
            @RequestParam("key") final String key,
            @RequestParam("translation") final String translation,
            @RequestParam("language") final Locale language) {

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
    @GetMapping(PATH_MESSAGES_COPY)
    public MessageImpl copy(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret,
            @RequestParam("sourceKey") final String sourceKey,
            @RequestParam("targetKey") final String targetKey) {

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
    @GetMapping(PATH_MESSAGES_REMOVE)
    public void remove(
            @RequestParam("projectId") final String projectId,
            @RequestParam("projectSecret") final String projectSecret,
            @RequestParam("key") final String key) {

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
