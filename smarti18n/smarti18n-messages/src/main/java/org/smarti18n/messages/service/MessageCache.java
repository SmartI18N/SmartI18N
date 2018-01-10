package org.smarti18n.messages.service;

import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.entities.MessageEntity;

import java.util.Collection;

public interface MessageCache {

    String CACHE_FIND_BY_PROJECT_ID = "findByProjectId";
    String CACHE_FIND_BY_USERNAME_AND_PROJECT_ID = "findByUsernameAndProjectId";

    Collection<MessageEntity> findByUsernameAndProjectId(
            String username,
            String projectId
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    Collection<MessageEntity> findByProjectId(
            String projectId
    ) throws ProjectUnknownException;
}
