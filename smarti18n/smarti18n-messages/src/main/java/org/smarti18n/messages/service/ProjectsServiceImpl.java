package org.smarti18n.messages.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.smarti18n.api.Project;
import org.smarti18n.api.ProjectImpl;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.entities.UserEntity;
import org.smarti18n.messages.repositories.ProjectRepository;

import static org.smarti18n.messages.service.IdentifierUtils.clean;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectRepository projectRepository;
    private final SmartKeyGenerator smartKeyGenerator;
    private final EntityLoader entityLoader;

    public ProjectsServiceImpl(
            final ProjectRepository projectRepository,
            final SmartKeyGenerator smartKeyGenerator,
            final EntityLoader entityLoader) {

        this.projectRepository = projectRepository;
        this.smartKeyGenerator = smartKeyGenerator;
        this.entityLoader = entityLoader;
    }

    @Override
    @Transactional
    public List<? extends Project> findAll(final String username) {
        final UserEntity user = this.entityLoader.findUser(username);

        return this.projectRepository.findByOwners(user);
    }

    @Override
    @Transactional
    public Project findOne(final String username, final String projectId) {
        final UserEntity user = this.entityLoader.findUser(username);

        final Optional<ProjectEntity> optional = this.projectRepository.findById(clean(projectId));
        if (!optional.isPresent()) {
            return null;
        }

        final ProjectEntity project = optional.get();
        if (project.hasOwner(user)) {
            return project;
        }

        throw new IllegalStateException(
                "User with Mail [" + clean(username) + "] hasn't rights for project [" + clean(projectId) + "]"
        );
    }

    @Override
    @Transactional
    public Project insert(final String username, final String projectId) {

        final String cleanedProjectId = clean(projectId);

        if (this.projectRepository.findById(cleanedProjectId).isPresent()) {
            throw new IllegalStateException("Project with id [" + cleanedProjectId + "] already exist.");
        }
        final String secret = this.smartKeyGenerator.generateKey();

        final ProjectEntity projectEntity = this.projectRepository.insert(
                new ProjectEntity(cleanedProjectId, secret, this.entityLoader.findUser(username))
        );

        return new ProjectImpl(
                projectEntity
        );
    }

    @Override
    @Transactional
    public Project update(final String username, final Project project) {
        final ProjectEntity projectEntity = this.entityLoader.findProject(username, project.getId());

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
    public void remove(final String username, final String projectId) {
        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        this.projectRepository.delete(project);
    }
}