package org.smarti18n.api.v2;

import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectCreateDTO;
import org.smarti18n.models.ProjectUpdateDTO;

import java.util.List;

public interface ProjectsApi {

    String PATH_PROJECTS_FIND_ALL = "/api/2/projects";
    String PATH_PROJECTS_FIND_ONE = "/api/2/projects/{projectId}";
    String PATH_PROJECTS_CREATE = "/api/2/projects";
    String PATH_PROJECTS_UPDATE = "/api/2/projects/{projectId}";
    String PATH_PROJECTS_REMOVE = "/api/2/projects/{projectId}";

    List<Project> findAll() throws UserUnknownException;

    Project findOne(
            String projectId
    ) throws UserUnknownException, UserRightsException;

    Project create(
            ProjectCreateDTO dto
    ) throws UserUnknownException, ProjectExistException;

    Project update(
            String projectId,
            ProjectUpdateDTO dto
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    void remove(
            String projectId
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;
}
