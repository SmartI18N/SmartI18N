package org.smarti18n.messages.projects;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.smarti18n.messages.common.EntityLoaderImpl;
import org.smarti18n.messages.common.SmartKeyGenerator;
import org.smarti18n.models.ProjectCreateDTO;
import org.smarti18n.models.ProjectUpdateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.exceptions.ProjectExistException;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.users.UserEntity;
import org.smarti18n.models.Project;
import org.smarti18n.models.ProjectImpl;
import org.smarti18n.models.UserRole;
import org.springframework.util.StringUtils;

import static org.smarti18n.messages.common.IdentifierUtils.clean;

@Service
public class ProjectsService {

    private final ProjectRepository projectRepository;
    private final SmartKeyGenerator smartKeyGenerator;
    private final EntityLoaderImpl entityLoader;

    private final Logger logger = LoggerFactory.getLogger(ProjectsService.class);

    public ProjectsService(
            final ProjectRepository projectRepository,
            final SmartKeyGenerator smartKeyGenerator,
            final EntityLoaderImpl entityLoader) {

        this.projectRepository = projectRepository;
        this.smartKeyGenerator = smartKeyGenerator;
        this.entityLoader = entityLoader;
    }

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

    @Transactional
    public Project insert(final String username, final ProjectCreateDTO dto) throws ProjectExistException, UserUnknownException {

        final String cleanedProjectId = clean(dto.getProjectId());

        if (this.projectRepository.findById(cleanedProjectId).isPresent()) {
            this.logger.error(
                    "Project with id [" + cleanedProjectId + "] already exist."
            );

            throw new ProjectExistException();
        }

        final String secret = this.smartKeyGenerator.generateKey();

        final ProjectEntity newProject = new ProjectEntity(
                cleanedProjectId,
                secret,
                this.entityLoader.findUser(username)
        );

        if (!StringUtils.isEmpty(dto.getParentProjectId())) {
            final ProjectEntity parentProject = this.entityLoader.findProject(username, dto.getParentProjectId());

            newProject.setParentProject(parentProject);
        }

        final ProjectEntity projectEntity = this.projectRepository.insert(newProject);

        return new ProjectImpl(
                projectEntity
        );
    }

    @Transactional
    public Project update(final String username, final String projectId, final ProjectUpdateDTO dto) throws ProjectUnknownException, UserUnknownException, UserRightsException {
        final ProjectEntity projectEntity = this.entityLoader.findProject(username, projectId);

        projectEntity.setName(dto.getName());
        projectEntity.setDescription(dto.getDescription());
        projectEntity.setLocales(
                dto.getLocales() == null ? new HashSet<>() : new HashSet<>(dto.getLocales())
        );

        return new ProjectImpl(
                this.projectRepository.save(projectEntity)
        );
    }

    public void remove(final String username, final String projectId) throws ProjectUnknownException, UserUnknownException, UserRightsException {
        final ProjectEntity project = this.entityLoader.findProject(username, projectId);

        this.projectRepository.delete(project);
    }
}
