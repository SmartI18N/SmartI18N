package org.smarti18n.api;

import java.util.List;

public interface ProjectsApi {

    String PATH_PROJECTS_FIND_ALL = "/api/1/projects/findAll";
    String PATH_PROJECTS_FIND_ONE = "/api/1/projects/findOne";
    String PATH_PROJECTS_INSERT = "/api/1/projects/insert";
    String PATH_PROJECTS_UPDATE = "/api/1/projects/update";
    String PATH_PROJECTS_REMOVE = "/api/1/projects/remove";

    List<? extends Project> findAll();

    Project findOne(
            String projectId
    );

    Project insert(
            String projectId
    );

    Project update(
            Project project
    );

    void remove(String projectId);
}
