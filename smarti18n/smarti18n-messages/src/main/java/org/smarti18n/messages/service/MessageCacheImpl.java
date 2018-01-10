package org.smarti18n.messages.service;

import java.util.Collection;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.entities.MessageEntity;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.MessageRepository;

@Component
public class MessageCacheImpl implements MessageCache {

    private final Logger logger = LoggerFactory.getLogger(MessagesServiceImpl.class);

    private final MessageRepository messageRepository;
    private final EntityLoader entityLoader;

    public MessageCacheImpl(final MessageRepository messageRepository, final EntityLoader entityLoader) {
        this.messageRepository = messageRepository;
        this.entityLoader = entityLoader;
    }


    @Override
    public Collection<MessageEntity> findByProjectId(
            final String projectId) throws ProjectUnknownException {

        final ProjectEntity project = this.entityLoader.findProject(projectId);

        return findByProject(project);
    }

    @Override
    public Collection<MessageEntity> findByUsernameAndProjectId(
            final String username,
            final String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException {

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
