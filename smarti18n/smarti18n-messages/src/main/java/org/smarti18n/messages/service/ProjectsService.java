package org.smarti18n.messages.service;

import java.util.List;

import org.smarti18n.api.Project;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public interface ProjectsService {
    List<? extends Project> findAll();

    Project findOne(String projectId);

    Project insert(String projectId);

    Project update(Project project);

    String generateSecret(String projectId);

    void remove(String projectId);
}
