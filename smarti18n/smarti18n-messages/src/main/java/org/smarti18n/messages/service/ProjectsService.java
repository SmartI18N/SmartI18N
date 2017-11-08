package org.smarti18n.messages.service;

import java.util.List;

import org.smarti18n.api.Project;

/**
 * @author Marc Bellmann &lt;marc.bellmann@saxess.ag&gt;
 */
public interface ProjectsService {
    List<? extends Project> findAll();

    Project insert(String projectId);

    Project update(Project project);

    String generateSecret(String projectId);
}
