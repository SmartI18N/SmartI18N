package org.smarti18n.messages.projects;

import org.smarti18n.api2.ProjectsApi;
import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.security.SecurityUtils;
import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectCreateDTO;
import org.smarti18n.models.ProjectUpdateDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Projects2Endpoint implements ProjectsApi {

    private final ProjectsService projectsService;

    public Projects2Endpoint(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ALL)
    public List<Project> findAll() throws UserUnknownException {

        return projectsService.findAll(
                SecurityUtils.getUserId()
        );
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ONE)
    public Project findOne(
            @PathVariable("projectId") String projectId) throws UserUnknownException, UserRightsException {

        return projectsService.findOne(
                SecurityUtils.getUserId(),
                projectId
        );
    }

    @Override
    @PostMapping(PATH_PROJECTS_CREATE)
    public Project create(
            @RequestBody ProjectCreateDTO dto) throws UserUnknownException, ProjectExistException {

        return projectsService.insert(
                SecurityUtils.getUserId(),
                dto
        );
    }

    @Override
    @PutMapping(PATH_PROJECTS_UPDATE)
    public Project update(
            @PathVariable("projectId") String projectId,
            @RequestBody ProjectUpdateDTO dto) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        return projectsService.update(
                SecurityUtils.getUserId(),
                projectId,
                dto
        );
    }

    @Override
    @DeleteMapping(PATH_PROJECTS_REMOVE)
    public void remove(
            @PathVariable("projectId") String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        projectsService.remove(
                SecurityUtils.getUserId(),
                projectId
        );
    }
}
