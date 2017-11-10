package org.smarti18n.editor.utils;

import org.springframework.util.StringUtils;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectContext {

    private String projectId;

    public String get() {
        return projectId;
    }

    public void setProjectId(final String projectId) {
        if (StringUtils.isEmpty(projectId)) {
            throw new IllegalStateException("Project ID not set");
        }

        this.projectId = projectId;
    }
}
