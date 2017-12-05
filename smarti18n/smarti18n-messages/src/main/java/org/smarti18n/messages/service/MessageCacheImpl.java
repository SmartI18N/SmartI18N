package org.smarti18n.messages.service;

import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.MessageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class MessageCacheImpl implements MessageCache {

    private final Logger logger = LoggerFactory.getLogger(MessagesServiceImpl.class);

    private final MessageRepository messageRepository;
    private final EntityLoader entityLoader;

    public MessageCacheImpl(final MessageRepository messageRepository, final EntityLoader entityLoader) {
        this.messageRepository = messageRepository;
        this.entityLoader = entityLoader;
    }


    @Cacheable(CACHE_FIND_BY_PROJECT_ID)
    @Override
    public Collection<MessageEntity> findByProjectId(
            final String projectId) {

        final ProjectEntity project = this.entityLoader.findProject(projectId);

        return findByProject(project);
    }

    @Cacheable(CACHE_FIND_BY_USERNAME_AND_PROJECT_ID)
    @Override
    public Collection<MessageEntity> findByUsernameAndProjectId(
            final String username,
            final String projectId) {

        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        return findByProject(project);
    }

    private Collection<MessageEntity> findByProject(final ProjectEntity project) {
        final Long startTime = System.currentTimeMillis();

        final Collection<MessageEntity> messages = this.messageRepository.findByIdProject(project);

        final Long endTime = System.currentTimeMillis();

        this.logger.info("find messages by project in " + (endTime - startTime) + "ms");

        return messages;
    }
}
