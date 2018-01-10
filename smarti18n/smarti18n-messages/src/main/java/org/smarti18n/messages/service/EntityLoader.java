package org.smarti18n.messages.service;

import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.entities.UserEntity;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface EntityLoader {

    UserEntity findUser(String username) throws UserUnknownException;

    ProjectEntity findProject(String username, String projectId) throws UserUnknownException, UserRightsException, ProjectUnknownException;

    ProjectEntity findProject(String projectId) throws ProjectUnknownException;
}
