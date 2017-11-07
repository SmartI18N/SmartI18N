package org.smarti18n.messages.controller;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.MessageImpl;
import org.smarti18n.api.MessagesApi;
import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.MessageRepository;
import org.smarti18n.messages.repositories.ProjectRepository;

@RestController
public class MessagesController implements MessagesApi {

    private final MessageRepository messageRepository;
    private final ProjectRepository projectRepository;

    private final ProjectContext projectContext;

    public MessagesController(
            final MessageRepository messageRepository,
            final ProjectRepository projectRepository,
            final ProjectContext projectContext) {

        this.messageRepository = messageRepository;
        this.projectRepository = projectRepository;
        this.projectContext = projectContext;
    }

    @Override
    @GetMapping(PATH_MESSAGES_FIND_ALL)
    public Collection<MessageImpl> findAll() {

        final ProjectEntity project = getProject();

        return this.messageRepository.findByIdProject(project).stream().map(messageEntity -> new MessageImpl(
                messageEntity.getKey(),
                messageEntity.getTranslations()
        )).collect(Collectors.toList());
    }

    @Override
    @GetMapping(PATH_MESSAGES_INSERT)
    public MessageImpl insert(
            @RequestParam("key") final String key) {

        final ProjectEntity project = getProject();

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
            @RequestParam("key") final String key,
            @RequestParam("translation") final String translation,
            @RequestParam("language") final Locale language) {

        final ProjectEntity project = getProject();

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
            @RequestParam("sourceKey") final String sourceKey,
            @RequestParam("targetKey") final String targetKey) {

        final ProjectEntity project = getProject();
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
            @RequestParam("key") final String key) {

        this.messageRepository.deleteById(new MessageEntity.MessageId(key, getProject()));
    }

    private ProjectEntity getProject() {
        final String projectId = this.projectContext.getProjectId();
        final String projectSecret = this.projectContext.getProjectSecret();

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
