package org.smarti18n.messages.controller;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.ProjectRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

@RestController
public class ProjectsController implements ProjectsApi {

    private final ProjectRepository projectRepository;

    public ProjectsController(final ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ALL)
    public Collection<? extends Project> findAll() {
        return this.projectRepository.findAll();
    }

    @Override
    @PostMapping(PATH_PROJECTS_SAVE)
    public Project save(
            @RequestBody final Project project) {

        final ProjectEntity projectEntity = this.projectRepository.findById(project.getId())
                .orElseGet(() -> new ProjectEntity(project.getId()));

        projectEntity.setName(project.getName());
        projectEntity.setDescription(project.getDescription());
        projectEntity.setLocales(
                project.getLocales() == null ? new HashSet<>() : new HashSet<>(project.getLocales())
        );

        return new ProjectImpl(
                this.projectRepository.save(projectEntity)
        );
    }

}
