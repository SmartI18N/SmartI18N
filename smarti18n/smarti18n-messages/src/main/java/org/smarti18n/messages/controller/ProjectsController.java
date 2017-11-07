package org.smarti18n.messages.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.KeyGenerator;
import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.api.ProjectsApi;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.ProjectRepository;

@RestController
public class ProjectsController implements ProjectsApi {

    private final ProjectRepository projectRepository;

    private final KeyGenerator keyGenerator;

    public ProjectsController(final ProjectRepository projectRepository) throws NoSuchAlgorithmException {
        this.projectRepository = projectRepository;
        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(256);
    }

    @Override
    @GetMapping(PATH_PROJECTS_FIND_ALL)
    public List<? extends Project> findAll() {
        return this.projectRepository.findAll();
    }

    @Override
    @GetMapping(PATH_PROJECTS_INSERT)
    public Project insert(
            @RequestParam("projectId") final String projectId) {

        if (this.projectRepository.findById(projectId).isPresent()) {
            throw new IllegalStateException("Project with id [" + projectId + "] already exist.");
        }

        final ProjectEntity projectEntity = this.projectRepository.insert(new ProjectEntity(projectId));

        return new ProjectImpl(
                projectEntity
        );
    }

    @Override
    @PostMapping(PATH_PROJECTS_UPDATE)
    public Project update(
            @RequestBody final Project project) {

        final Optional<ProjectEntity> optional = this.projectRepository.findById(project.getId());
        if (!optional.isPresent()) {
            throw new IllegalStateException("Project with id [" + project.getId() + "] doesn't exist.");
        }

        final ProjectEntity projectEntity = optional.get();

        projectEntity.setName(project.getName());
        projectEntity.setDescription(project.getDescription());
        projectEntity.setLocales(
                project.getLocales() == null ? new HashSet<>() : new HashSet<>(project.getLocales())
        );

        return new ProjectImpl(
                this.projectRepository.save(projectEntity)
        );
    }

    @Override
    @GetMapping(PATH_PROJECTS_GENERATE_SECRET)
    public String generateSecret(
            @RequestParam("projectId") final String projectId) {

        final Optional<ProjectEntity> optional = this.projectRepository.findById(projectId);
        if (!optional.isPresent()) {
            throw new IllegalStateException("Project with id [" + projectId + "] doesn't exist.");
        }

        final ProjectEntity projectEntity = optional.get();

        final String secret = Base64.getEncoder().encodeToString(
                this.keyGenerator.generateKey().getEncoded()
        );

        projectEntity.getSecrets().add(secret);

        this.projectRepository.save(projectEntity);

        return secret;
    }
}