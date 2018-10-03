package org.smarti18n.messages.service;

import java.util.List;

import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Project;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface ProjectsService {
    List<Project> findAll(String username) throws UserUnknownException;

    Project findOne(String username, String projectId) throws UserRightsException, UserUnknownException;

    Project insert(String username, String projectId, String parentProjectId) throws ProjectExistException, UserUnknownException;

    Project update(String username, Project project) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    void remove(String username, String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException;
}
