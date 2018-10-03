package org.smarti18n.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectImpl;
import org.smarti18n.models.UserCredentialsSupplier;

public class ProjectsApiImpl extends AbstractApiImpl implements ProjectsApi {

    public ProjectsApiImpl(
            final RestTemplate restTemplate,
            final String host,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, host, userCredentialsSupplier);
    }

    public ProjectsApiImpl(
            final RestTemplate restTemplate,
            final int port,
            final UserCredentialsSupplier userCredentialsSupplier) {

        super(restTemplate, port, userCredentialsSupplier);
    }

    @Override
    public List<Project> findAll() {
        return Arrays.asList(
                get(uri(ProjectsApi.PATH_PROJECTS_FIND_ALL), Project[].class)
        );
    }

    @Override
    public Project findOne(final String projectId) {
        return get(uri(ProjectsApi.PATH_PROJECTS_FIND_ONE).queryParam("projectId", projectId), ProjectImpl.class);
    }

    @Override
    public Project insert(final String projectId, final String parentProjectId) {
        return get(
                uri(ProjectsApi.PATH_PROJECTS_INSERT)
                        .queryParam("projectId", projectId)
                        .queryParam("parentProjectId", parentProjectId),
                ProjectImpl.class
        );
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
