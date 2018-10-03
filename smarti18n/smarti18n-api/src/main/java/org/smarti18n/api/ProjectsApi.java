package org.smarti18n.api;

import java.util.List;

import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.models.Project;

public interface ProjectsApi {

    String PATH_PROJECTS_FIND_ALL = "/api/1/projects/findAll";
    String PATH_PROJECTS_FIND_ONE = "/api/1/projects/findOne";
    String PATH_PROJECTS_INSERT = "/api/1/projects/insert";
    String PATH_PROJECTS_UPDATE = "/api/1/projects/update";
    String PATH_PROJECTS_REMOVE = "/api/1/projects/remove";

    List<Project> findAll() throws UserUnknownException;

    Project findOne(
            String projectId
    ) throws UserUnknownException, UserRightsException;

    Project insert(
            String projectId,
            String parentProjectId
    ) throws UserUnknownException, ProjectExistException;

    Project update(
            Project project
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException;

    void remove(String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException;
}
