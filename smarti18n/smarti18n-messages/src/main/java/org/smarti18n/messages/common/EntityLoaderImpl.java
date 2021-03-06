package org.smarti18n.messages.common;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarti18n.exceptions.ProjectUnknownException;
import org.smarti18n.exceptions.UserRightsException;
import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.projects.ProjectEntity;
import org.smarti18n.messages.users.UserEntity;
import org.smarti18n.messages.projects.ProjectRepository;
import org.smarti18n.messages.users.UserRepository;

import static org.smarti18n.messages.common.IdentifierUtils.clean;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Component
public class EntityLoaderImpl {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    private final Logger logger = LoggerFactory.getLogger(EntityLoaderImpl.class);

    public EntityLoaderImpl(final UserRepository userRepository, final ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }


    public UserEntity findUser(final String username) throws UserUnknownException {
        final Optional<UserEntity> user = this.userRepository.findByMail(clean(username));

        if (user.isPresent()) {
            return user.get();
        }

        this.logger.error("User with Mail [" + clean(username) + "] doesn't exist.");

        throw new UserUnknownException();
    }

    public ProjectEntity findProject(final String username, final String projectId) throws UserUnknownException, UserRightsException, ProjectUnknownException {
        Assert.notNull(projectId, "projectId");

        final ProjectEntity project = findProject(clean(projectId));
        final Optional<UserEntity> user = this.userRepository.findByMail(clean(username));

        if (!user.isPresent()) {
            this.logger.error("User with Mail [" + clean(username) + "] doesn't exist");

            throw new UserUnknownException();
        }

        if (!project.hasOwner(user.get())) {
            this.logger.error("User with Mail [" + clean(username) + "] hasn't rights for project [" + clean(projectId) + "]");

            throw new UserRightsException();
        }

        return project;
    }

    public ProjectEntity findProject(final String projectId) throws ProjectUnknownException {
        final Optional<ProjectEntity> optional = this.projectRepository.findById(clean(projectId));

        if (optional.isPresent()) {
            return optional.get();
        }

        this.logger.error("Project with ID [" + clean(projectId) + "] doesn't exist.");

        throw new ProjectUnknownException();
    }
}
