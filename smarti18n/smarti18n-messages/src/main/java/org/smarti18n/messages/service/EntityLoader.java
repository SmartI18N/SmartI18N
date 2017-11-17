package org.smarti18n.messages.service;

import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.entities.UserEntity;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface EntityLoader {

    UserEntity findUser(String username);

    ProjectEntity findProject(String username, String projectId);

    ProjectEntity findProject(String projectId);
}
