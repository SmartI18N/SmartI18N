package org.smarti18n.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

public class ProjectsApiImpl extends AbstractApiImpl implements ProjectsApi {

    public ProjectsApiImpl(final Environment environment, final RestTemplate restTemplate) {
        super(restTemplate, environment);
    }

    public ProjectsApiImpl(final RestTemplate restTemplate, final int port) {
        super(restTemplate, "http://localhost:" + port, "test", "test");
    }

    @Override
    public List<? extends Project> findAll() {
        return Arrays.asList(
                get(uri(ProjectsApi.PATH_PROJECTS_FIND_ALL), ProjectImpl[].class)
        );
    }

    @Override
    public Project findOne(final String projectId) {
        return get(uri(ProjectsApi.PATH_PROJECTS_FIND_ONE).queryParam("projectId", projectId), ProjectImpl.class);
    }

    @Override
    public Project insert(final String projectId) {
        return get(uri(ProjectsApi.PATH_PROJECTS_INSERT).queryParam("projectId", projectId), ProjectImpl.class);
    }

    @Override
    public Project update(final Project project) {
        return post(uri(ProjectsApi.PATH_PROJECTS_UPDATE), project, ProjectImpl.class);
    }

    @Override
    public void remove(final String projectId) {
        get(uri(ProjectsApi.PATH_PROJECTS_REMOVE).queryParam("projectId", projectId), Void.class);
    }
}
