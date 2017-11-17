package org.smarti18n.messages.service;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.smarti18n.messages.entities.ProjectEntity;
import org.smarti18n.messages.entities.UserEntity;
import org.smarti18n.messages.repositories.ProjectRepository;
import org.smarti18n.messages.repositories.UserRepository;

import static org.smarti18n.messages.service.IdentifierUtils.clean;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Component
public class EntityLoaderImpl implements EntityLoader {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public EntityLoaderImpl(final UserRepository userRepository, final ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }


    @Override
    public UserEntity findUser(final String username) {
        final Optional<UserEntity> user = this.userRepository.findByMail(clean(username));

        if (user.isPresent()) {
            return user.get();
        }

        throw new IllegalStateException("User with Mail [" + clean(username) + "] doesn't exist.");
    }

    @Override
    public ProjectEntity findProject(final String username, final String projectId) {
        Assert.notNull(projectId, "projectId");

        final ProjectEntity project = findProject(clean(projectId));
        final Optional<UserEntity> user = this.userRepository.findByMail(clean(username));

        if (!user.isPresent()) {
            throw new IllegalStateException("User with Mail [" + clean(username) + "] doesn't exist");
        }

        if (!project.hasOwner(user.get())) {
            throw new IllegalStateException(
                    "User with Mail [" + clean(username) + "] hasn't rights for project [" + clean(projectId) + "]"
            );
        }

        return project;
    }

    @Override
    public ProjectEntity findProject(final String projectId) {
        final Optional<ProjectEntity> optional = this.projectRepository.findById(clean(projectId));

        if (optional.isPresent()) {
            return optional.get();
        }

        throw new IllegalStateException("Project with ID [" + clean(projectId) + "] doesn't exist.");
    }
}
