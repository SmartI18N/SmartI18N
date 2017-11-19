package org.smarti18n.editor.utils;

import org.springframework.util.Assert;

import org.smarti18n.api.Project;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectContext {

    private Project project;
    private String projectId;

    public void setProject(final Project project) {
        Assert.notNull(project, "project");

        this.project = project;
    }

    public Project getProject() {
        Assert.notNull(project, "project");

        return project;
    }

    public void setProjectId(final String projectId) {
        Assert.notNull(projectId, "projectId");

        this.projectId = projectId;
    }

    public String getProjectId() {
        if (projectId == null) {
            final Project project = getProject();

            return project.getId();
        }

        Assert.notNull(projectId, "projectId");
        return projectId;
    }
}
