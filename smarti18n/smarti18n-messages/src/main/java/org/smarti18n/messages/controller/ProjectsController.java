package org.smarti18n.messages.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.messages.service.ProjectsService;

@RestController
public class ProjectsController implements ProjectsApi {

    private final ProjectsService projectsService;

    public ProjectsController(final ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ALL)
    public List<? extends Project> findAll() {
        return projectsService.findAll();
    }

    @Override
    @GetMapping(PATH_PROJECTS_INSERT)
    public Project insert(
            @RequestParam("projectId") final String projectId) {

        return projectsService.insert(projectId);
    }

    @Override
    @PostMapping(PATH_PROJECTS_UPDATE)
    public Project update(
            @RequestBody final Project project) {

        return projectsService.update(project);
    }

    @Override
    @GetMapping(PATH_PROJECTS_GENERATE_SECRET)
    public String generateSecret(
            @RequestParam("projectId") final String projectId) {

        return projectsService.generateSecret(projectId);
    }

    @Override
    @GetMapping(PATH_PROJECTS_REMOVE)
    public void remove(
            @RequestParam("projectId") final String projectId) {

        this.projectsService.remove(projectId);
    }
}
