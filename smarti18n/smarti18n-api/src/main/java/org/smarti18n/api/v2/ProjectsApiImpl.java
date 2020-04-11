package org.smarti18n.api.v2;

import org.smarti18n.api.AbstractApiImpl;
import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectCreateDTO;
import org.smarti18n.models.ProjectUpdateDTO;
import org.smarti18n.models.UserCredentialsSupplier;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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
    public List<Project> findAll() throws UserUnknownException {
        return Arrays.asList(
                get(uri(PATH_PROJECTS_FIND_ALL), Project[].class)
        );
    }

    @Override
    public Project findOne(String projectId) throws UserUnknownException, UserRightsException {
        return get(uri(path(PATH_PROJECTS_FIND_ONE, projectId)), Project.class);
    }

    @Override
    public Project create(ProjectCreateDTO dto) throws UserUnknownException, ProjectExistException {
        return post(uri(PATH_PROJECTS_CREATE), dto, Project.class);
    }

    @Override
    public Project update(String projectId, ProjectUpdateDTO dto) throws ProjectUnknownException, UserUnknownException, UserRightsException {
        return put(uri(path(PATH_PROJECTS_UPDATE, projectId)), dto, Project.class);
    }

    @Override
    public void remove(String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException {
        delete(uri(path(PATH_PROJECTS_REMOVE, projectId)), Void.class);
    }

    private static String path(String path, String projectId) {
        return path.replace("{projectId}", projectId);
    }
}
