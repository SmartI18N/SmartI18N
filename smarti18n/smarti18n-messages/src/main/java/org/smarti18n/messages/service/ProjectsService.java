package org.smarti18n.messages.service;

import java.util.List;

import org.smarti18n.api.Project;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface ProjectsService {
    List<Project> findAll(final String username);

    Project findOne(final String username, String projectId);

    Project insert(final String username, String projectId);

    Project update(final String username, Project project);

    void remove(final String username, String projectId);
}
