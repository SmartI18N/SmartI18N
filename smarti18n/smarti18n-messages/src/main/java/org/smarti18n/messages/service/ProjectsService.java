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
    List<Project> findAll(final String username) throws UserUnknownException;

    Project findOne(final String username, String projectId) throws UserRightsException, UserUnknownException;

    Project insert(final String username, String projectId) throws ProjectExistException, UserUnknownException;

    Project update(final String username, Project project) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    void remove(final String username, String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException;
}
