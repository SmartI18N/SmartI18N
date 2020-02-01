package org.smarti18n.messages.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.exceptions.MessageExistException;
import org.smarti18n.exceptions.MessageUnknownException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.common.EntityLoaderImpl;
import org.smarti18n.messages.projects.ProjectEntity;
import org.smarti18n.models.Message;
import org.smarti18n.models.MessageCreateDTO;
import org.smarti18n.models.MessageImpl;
import org.smarti18n.models.MessageUpdateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.smarti18n.messages.common.IdentifierUtils.clean;

@Service
public class MessagesService {

    private final MessageCache messageCache;
    private final MessageRepository messageRepository;
    private final EntityLoaderImpl entityLoader;

    private final Logger logger = LoggerFactory.getLogger(MessagesService.class);

    public MessagesService(
            final MessageCache messageCache,
            final MessageRepository messageRepository,
            final EntityLoaderImpl entityLoader) {

        this.messageCache = messageCache;
        this.messageRepository = messageRepository;
        this.entityLoader = entityLoader;
    }

    @Transactional
    public Collection<Message> findAll(final String username, final String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        final Collection<MessageEntity> messages = this.messageCache.findByUsernameAndProjectId(
                username,
                projectId
        );

        return messages.stream().map(messageEntity -> new MessageImpl(
                messageEntity.getKey(),
                messageEntity.getTranslations()
        ))
                .sorted(Comparator.comparing(MessageImpl::getKey))
                .collect(Collectors.toList());
    }

    @Transactional
    public Message findOne(final String username, final String projectId, final String key) throws ProjectUnknownException, UserUnknownException, UserRightsException {
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

    @Transactional
    public Message insert(
            final String username,
            final String projectId,
            final MessageCreateDTO dto) throws MessageExistException, ProjectUnknownException, UserUnknownException, UserRightsException {

        final String cleanKey = clean(dto.getKey());

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        if (this.messageRepository.findById(new MessageEntity.MessageId(cleanKey, project)).isPresent()) {
            this.logger.error("Message with key [" + cleanKey + "] already exist.");

            throw new MessageExistException();
        }

        return new MessageImpl(
                this.messageRepository.insert(new MessageEntity(cleanKey, project))
        );
    }

    @Transactional
    public Message update(
            final String username, final String projectId,
            final String key,
            final Locale locale, final String translation) throws ProjectUnknownException, UserUnknownException, UserRightsException {

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

    @Transactional
    public Message update(final String username, final String projectId, final String messageKey, final MessageUpdateDTO dto) throws MessageUnknownException, ProjectUnknownException, UserUnknownException, UserRightsException {
        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        final Optional<MessageEntity> optional = this.messageRepository.findById(
                new MessageEntity.MessageId(messageKey, project)
        );

        if (!optional.isPresent()) {
            this.logger.error("Message with key [" + messageKey + "] and Project [" + projectId + "] doesn't exist!");

            throw new MessageUnknownException();
        }
        final MessageEntity messageEntity = optional.get();

        messageEntity.setTranslations(dto.getTranslations());

        return this.messageRepository.save(messageEntity);
    }

    @Transactional
    public Message copy(
            final String username, final String projectId,
            final String sourceKey,
            final String targetKey) throws MessageExistException, MessageUnknownException, ProjectUnknownException, UserUnknownException, UserRightsException {

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        final Optional<MessageEntity> optional = this.messageRepository.findById(new MessageEntity.MessageId(clean(sourceKey), project));

        if (!optional.isPresent()) {
            this.logger.error("Message with key [" + sourceKey + "] and Project [" + projectId + "] doesn't exist!");

            throw new MessageUnknownException();
        }

        final String cleanedTargetKey = clean(targetKey);

        if (this.messageRepository.findById(new MessageEntity.MessageId(cleanedTargetKey, project)).isPresent()) {
            this.logger.error("Message with key [" + cleanedTargetKey + "] already exist.");

            throw new MessageExistException();
        }

        final MessageEntity messageEntity = optional.get();
        messageEntity.setKey(cleanedTargetKey);

        final MessageEntity saved = this.messageRepository.save(messageEntity);

        return new MessageImpl(
                saved.getKey(),
                saved.getTranslations()
        );

    }

    @Transactional
    public void remove(
            final String username, final String projectId,
            final String key) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        this.messageRepository.deleteById(new MessageEntity.MessageId(clean(key), project));
    }

    @Transactional
    public Map<String, Map<Locale, String>> findForSpringMessageSource(final String projectId) throws ProjectUnknownException {
        final Collection<MessageEntity> messages = getMessagesForSource(projectId);

        final Map<String, Map<Locale, String>> map = new HashMap<>();

        for (final MessageEntity message : messages) {
            map.put(message.getKey(), new HashMap<>(message.getTranslations()));
        }

        return map;
    }

    @Transactional
    public Map<String, String> findForAngularMessageSource(final String projectId, final Locale locale) throws ProjectUnknownException {
        final Collection<MessageEntity> messages = getMessagesForSource(projectId);

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

    private Collection<MessageEntity> getMessagesForSource(final String projectId) {
        final ProjectEntity project = this.entityLoader.findProject(projectId);

        return getMessagesForSource(project, new HashSet<>());
    }

    private Collection<MessageEntity> getMessagesForSource(ProjectEntity project, Set<String> circleControl) {
        String projectId = project.getId();
        if (circleControl.contains(projectId)) {
            throw new IllegalStateException("Circle detected! => " + circleControl);
        }
        circleControl.add(projectId);

        final LinkedHashSet<MessageEntity> messages = new LinkedHashSet<>();

        if (project.getParentProject() != null) {
            messages.addAll(getMessagesForSource(project.getParentProject(), circleControl));
        }

        messages.addAll(this.messageCache.findByProjectId(projectId));
        return messages;
    }

}
