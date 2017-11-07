package org.smarti18n.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

public class ProjectsApiImpl extends AbstractApiImpl implements ProjectsApi {

    public ProjectsApiImpl(final Environment environment, final RestTemplate restTemplate) {
        super(
                restTemplate, environment.getProperty("", DEFAULT_HOST),
                environment.getProperty("", DEFAULT_PROJECT_ID),
                environment.getProperty("", DEFAULT_PROJECT_ID));
    }

    public ProjectsApiImpl(final RestTemplate restTemplate, final int port) {
        super(restTemplate, "http://localhost:" + port, null, null);
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

    @Override
    public String generateSecret(final String projectId) {
        return get(ProjectsApi.PATH_PROJECTS_GENERATE_SECRET + "?projectId=" + projectId, String.class);
    }
}
