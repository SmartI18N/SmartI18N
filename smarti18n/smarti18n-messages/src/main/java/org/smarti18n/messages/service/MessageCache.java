package org.smarti18n.messages.service;

import org.smarti18n.messages.entities.MessageEntity;

import java.util.Collection;

public interface MessageCache {

    Collection<MessageEntity> findByUsernameAndProjectId(
            String username,
            String projectId
    );

    Collection<MessageEntity> findByProjectId(
            String projectId
    );
}
