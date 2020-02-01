package org.smarti18n.messages.messages;

import java.util.Collection;

import org.smarti18n.messages.common.EntityLoaderImpl;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.projects.ProjectEntity;

@Component
public class MessageCache {

    public static final String CACHE_FIND_BY_PROJECT_ID = "findByProjectId";
    public static final String CACHE_FIND_BY_USERNAME_AND_PROJECT_ID = "findByUsernameAndProjectId";
    private final Logger logger = LoggerFactory.getLogger(MessagesService.class);

    private final MessageRepository messageRepository;
    private final EntityLoaderImpl entityLoader;

    public MessageCache(final MessageRepository messageRepository, final EntityLoaderImpl entityLoader) {
        this.messageRepository = messageRepository;
        this.entityLoader = entityLoader;
    }


    public Collection<MessageEntity> findByProjectId(
            final String projectId) throws ProjectUnknownException {

        final ProjectEntity project = this.entityLoader.findProject(projectId);

        return findByProject(project);
    }

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
