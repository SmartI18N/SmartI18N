package org.smarti18n.api.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;

public class ProjectsApiImpl extends AbstractApiImpl implements ProjectsApi {

    public final static String DEFAULT_HOST = "https://messages.smarti18n.com";

    public ProjectsApiImpl(final Environment environment, final RestTemplate restTemplate) {
        super(environment.getProperty("", DEFAULT_HOST), restTemplate);
    }

    public ProjectsApiImpl(final RestTemplate restTemplate, final int port) {
        super("http://localhost:" + port, restTemplate);
    }

    @Override
    public List<? extends Project> findAll() {
        return Arrays.asList(
                get(ProjectsApi.PATH_PROJECTS_FIND_ALL, ProjectImpl[].class)
        );
    }

    @Override
    public Project insert(final String projectId) {
        return get(ProjectsApi.PATH_PROJECTS_INSERT + "?projectId=" + projectId, ProjectImpl.class);
    }

    @Override
    public Project update(final Project project) {
        return post(ProjectsApi.PATH_PROJECTS_UPDATE, project, ProjectImpl.class);
    }
}
