package org.smarti18n.messages.controller;

import org.springframework.lang.Nullable;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import org.smarti18n.api.MessagesApi;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectWebRequestInterceptor implements WebRequestInterceptor {

    private final ProjectContext projectContext;

    public ProjectWebRequestInterceptor(final ProjectContext projectContext) {
        this.projectContext = projectContext;
    }

    @Override
    public void preHandle(final WebRequest webRequest) throws Exception {
        final String projectId = webRequest.getHeader(MessagesApi.PROJECT_ID_HEADER);
        final String projectSecret = webRequest.getHeader(MessagesApi.PROJECT_SECRET_HEADER);

        projectContext.setProject(
                projectId,
                projectSecret
        );
    }

    @Override
    public void postHandle(final WebRequest webRequest, @Nullable final ModelMap modelMap) throws Exception {
        this.projectContext.destroy();
    }

    @Override
    public void afterCompletion(final WebRequest webRequest, @Nullable final Exception e) throws Exception {

    }
}
