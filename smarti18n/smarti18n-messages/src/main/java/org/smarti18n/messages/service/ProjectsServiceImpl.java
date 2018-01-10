package org.smarti18n.messages.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.entities.UserEntity;
import org.smarti18n.messages.repositories.ProjectRepository;
import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectImpl;
import org.smarti18n.models.UserRole;

import static org.smarti18n.messages.service.IdentifierUtils.clean;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectRepository projectRepository;
    private final SmartKeyGenerator smartKeyGenerator;
    private final EntityLoader entityLoader;

    private final Logger logger = LoggerFactory.getLogger(ProjectsServiceImpl.class);

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
    public List<Project> findAll(final String username) throws UserUnknownException {
        final UserEntity user = this.entityLoader.findUser(username);

        if (user.getRole() == UserRole.SUPERUSER) {
            return this.projectRepository.findAll()
                    .stream().collect(Collectors.toList());
        }

        return this.projectRepository.findByOwners(user)
                .stream().collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Project findOne(final String username, final String projectId) throws UserRightsException, UserUnknownException {
        final UserEntity user = this.entityLoader.findUser(username);

        final Optional<ProjectEntity> optional = this.projectRepository.findById(clean(projectId));
        if (!optional.isPresent()) {
            return null;
        }

        final ProjectEntity project = optional.get();
        if (project.hasOwner(user)) {
            return project;
        }

        this.logger.error("User with Mail [" + clean(username) + "] hasn't rights for project [" + clean(projectId) + "]");

        throw new UserRightsException();
    }

    @Override
    @Transactional
    public Project insert(final String username, final String projectId) throws ProjectExistException, UserUnknownException {

        final String cleanedProjectId = clean(projectId);

        if (this.projectRepository.findById(cleanedProjectId).isPresent()) {
            this.logger.error(
                    "Project with id [" + cleanedProjectId + "] already exist."
            );

            throw new ProjectExistException();
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
    public Project update(final String username, final Project project) throws ProjectUnknownException, UserUnknownException, UserRightsException {
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
    public void remove(final String username, final String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException {
        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        this.projectRepository.delete(project);
    }
}