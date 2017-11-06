package org.smarti18n.api;

import java.util.Collection;

public interface ProjectsApi {

    String PATH_PROJECTS_FIND_ALL = "/api/1/projects/findAll";
    String PATH_PROJECTS_SAVE = "/api/1/projects/save";

    Collection<? extends Project> findAll();

    Project save(
            Project project
    );
}
