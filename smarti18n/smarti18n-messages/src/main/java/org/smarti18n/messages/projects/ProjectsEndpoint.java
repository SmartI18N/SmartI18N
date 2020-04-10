package org.smarti18n.messages.projects;

import org.smarti18n.api.ProjectsApi;
import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.security.SecurityUtils;
import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectCreateDTO;
import org.smarti18n.models.ProjectUpdateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectsEndpoint implements ProjectsApi {

    private final ProjectsService projectsService;

    public ProjectsEndpoint(final ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ALL)
    public List<Project> findAll() throws UserUnknownException {

        return projectsService.findAll(
                SecurityUtils.getUserMail()
        );
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ONE)
    public Project findOne(
            @RequestParam("projectId") final String projectId
    ) throws UserUnknownException, UserRightsException {

        return projectsService.findOne(
                SecurityUtils.getUserMail(),
                projectId
        );
    }

    @Override
    @GetMapping(PATH_PROJECTS_INSERT)
    public Project insert(
            @RequestParam("projectId") final String projectId,
            @RequestParam(value = "parentProjectId", required = false) final String parentProjectId
    ) throws UserUnknownException, ProjectExistException {

        return projectsService.insert(
                SecurityUtils.getUserMail(),
                new ProjectCreateDTO(projectId, parentProjectId)
        );
    }

    @Override
    @PostMapping(PATH_PROJECTS_UPDATE)
    public Project update(
            @RequestBody final Project project
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        return projectsService.update(
                SecurityUtils.getUserMail(),
                project.getId(),
                new ProjectUpdateDTO(project.getName(), project.getDescription(), project.getLocales())
        );
    }

    @Override
    @GetMapping(PATH_PROJECTS_REMOVE)
    public void remove(
            @RequestParam("projectId") final String projectId
    ) throws ProjectUnknownException, UserUnknownException, UserRightsException {

        this.projectsService.remove(
                SecurityUtils.getUserMail(),
                projectId
        );
    }
}
