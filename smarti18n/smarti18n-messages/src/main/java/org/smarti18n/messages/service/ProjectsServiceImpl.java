package org.smarti18n.messages.service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.repositories.ProjectRepository;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectRepository projectRepository;
    private final ProjectKeyGenerator projectKeyGenerator;

    public ProjectsServiceImpl(
            final ProjectRepository projectRepository,
            final ProjectKeyGenerator projectKeyGenerator) {

        this.projectRepository = projectRepository;
        this.projectKeyGenerator = projectKeyGenerator;
    }

    @Override
    @Transactional
    public List<? extends Project> findAll() {
        return this.projectRepository.findAll();
    }

    @Override
    @Transactional
    public Project insert(final String projectId) {

        if (this.projectRepository.findById(projectId).isPresent()) {
            throw new IllegalStateException("Project with id [" + projectId + "] already exist.");
        }

        final ProjectEntity projectEntity = this.projectRepository.insert(new ProjectEntity(projectId));

        return new ProjectImpl(
                projectEntity
        );
    }

    @Override
    @Transactional
    public Project update(final Project project) {

        final Optional<ProjectEntity> optional = this.projectRepository.findById(project.getId());
        if (!optional.isPresent()) {
            throw new IllegalStateException("Project with id [" + project.getId() + "] doesn't exist.");
        }

        final ProjectEntity projectEntity = optional.get();

        projectEntity.setName(project.getName());
        projectEntity.setDescription(project.getDescription());
        projectEntity.setLocales(
                project.getLocales() == null ? new HashSet<Locale>() : new HashSet<Locale>(project.getLocales())
        );

        return new ProjectImpl(
                this.projectRepository.save(projectEntity)
        );
    }

    @Override
    @Transactional
    public String generateSecret(final String projectId) {

        final Optional<ProjectEntity> optional = this.projectRepository.findById(projectId);
        if (!optional.isPresent()) {
            throw new IllegalStateException("Project with id [" + projectId + "] doesn't exist.");
        }

        final ProjectEntity projectEntity = optional.get();

        final String secret = this.projectKeyGenerator.generateKey();

        projectEntity.getSecrets().add(secret);

        this.projectRepository.save(projectEntity);

        return secret;
    }

}