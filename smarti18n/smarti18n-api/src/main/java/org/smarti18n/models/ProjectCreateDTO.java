package org.smarti18n.models;

public class ProjectCreateDTO {

    private String projectId;
    private String parentProjectId;

    public ProjectCreateDTO() {
    }

    public ProjectCreateDTO(String projectId, String parentProjectId) {
        this.projectId = projectId;
        this.parentProjectId = parentProjectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getParentProjectId() {
        return parentProjectId;
    }

    public void setParentProjectId(String parentProjectId) {
        this.parentProjectId = parentProjectId;
    }
}
