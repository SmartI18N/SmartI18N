package org.smarti18n.messages.service;

import java.util.HashSet;
import java.util.List;
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
    public Project findOne(final String projectId) {
        return this.projectRepository.findById(clean(projectId)).orElse(null);
    }

    @Override
    @Transactional
    public Project insert(final String projectId) {

        final String cleanedProjectId = clean(projectId);

        if (this.projectRepository.findById(cleanedProjectId).isPresent()) {
            throw new IllegalStateException("Project with id [" + cleanedProjectId + "] already exist.");
        }
        final String secret = this.projectKeyGenerator.generateKey();

        final ProjectEntity projectEntity = this.projectRepository.insert(new ProjectEntity(cleanedProjectId, secret));

        return new ProjectImpl(
                projectEntity
        );
    }

    @Override
    @Transactional
    public Project update(final Project project) {

        final String cleanedProjectId = clean(project.getId());

        final Optional<ProjectEntity> optional = this.projectRepository.findById(cleanedProjectId);
        if (!optional.isPresent()) {
            throw new IllegalStateException("Project with id [" + cleanedProjectId + "] doesn't exist.");
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
    public void remove(final String projectId) {
        this.projectRepository.deleteById(clean(projectId));
    }

    private static String clean(final String projectId) {
        return projectId.trim().toLowerCase();
    }

}