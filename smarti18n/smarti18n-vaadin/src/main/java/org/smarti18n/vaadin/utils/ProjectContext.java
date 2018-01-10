package org.smarti18n.vaadin.utils;

import org.springframework.util.Assert;

import org.smarti18n.models.Project;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectContext {

    private Project project;

    public void setProject(final Project project) {
        Assert.notNull(project, "project");

        this.project = project;
    }

    public Project getProject() {
        Assert.notNull(project, "project");

        return project;
    }

    public String getProjectId() {
        final Project project = getProject();

        return project.getId();
    }
}
