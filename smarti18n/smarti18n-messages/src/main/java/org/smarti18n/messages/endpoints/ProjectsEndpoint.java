package org.smarti18n.messages.endpoints;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.messages.security.SecurityUtils;
import org.smarti18n.messages.service.ProjectsService;

@RestController
public class ProjectsEndpoint implements ProjectsApi {

    private final ProjectsService projectsService;

    public ProjectsEndpoint(final ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ALL)
    public List<? extends Project> findAll() {
        return projectsService.findAll(
                SecurityUtils.getUserId()
        );
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ONE)
    public Project findOne(
            @RequestParam("projectId") final String projectId) {

        return projectsService.findOne(
                SecurityUtils.getUserId(),
                projectId
        );
    }

    @Override
    @GetMapping(PATH_PROJECTS_INSERT)
    public Project insert(
            @RequestParam("projectId") final String projectId) {

        return projectsService.insert(
                SecurityUtils.getUserId(),
                projectId
        );
    }

    @Override
    @PostMapping(PATH_PROJECTS_UPDATE)
    public Project update(
            @RequestBody final Project project) {

        return projectsService.update(
                SecurityUtils.getUserId(),
                project
        );
    }

    @Override
    @GetMapping(PATH_PROJECTS_REMOVE)
    public void remove(
            @RequestParam("projectId") final String projectId) {

        this.projectsService.remove(
                SecurityUtils.getUserId(),
                projectId
        );
    }
}
