package org.smarti18n.messages.controller;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Service
@RequestScope
public class ProjectContext {

    private static final String DEFAULT_ID = "default";

    private String projectId;
    private String projectSecret;
    private boolean destroyed = false;

    String getProjectId() {
        if (this.destroyed) {
            throw new IllegalStateException("ProjectContext already destroyed!");
        }

        return projectId;
    }

    String getProjectSecret() {
        if (this.destroyed) {
            throw new IllegalStateException("ProjectContext already destroyed!");
        }

        return projectSecret;
    }

    void setProject(
            @Nullable final String projectId,
            @Nullable final String projectSecret) {

        if (StringUtils.isEmpty(projectId)) {
            this.projectId = DEFAULT_ID;
        } else {
            this.projectId = projectId;
        }

        if (StringUtils.isEmpty(projectSecret)) {
            this.projectSecret = DEFAULT_ID;
        } else {
            this.projectSecret = projectSecret;
        }
    }

    void destroy() {
        this.destroyed = true;
    }

}
