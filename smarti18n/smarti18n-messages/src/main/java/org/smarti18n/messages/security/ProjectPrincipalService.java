package org.smarti18n.messages.security;

import org.smarti18n.exceptions.UserUnknownException;
import org.smarti18n.messages.projects.ProjectEntity;
import org.smarti18n.messages.projects.ProjectRepository;
import org.smarti18n.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Component
public class ProjectPrincipalService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final ProjectRepository projectRepository;

    public ProjectPrincipalService(final ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<ProjectEntity> byId = this.projectRepository.findById(username);
        if (byId.isPresent()) {
            final ProjectEntity project = byId.get();
            final User user = project.getOwners().stream().findFirst().orElseThrow(UserUnknownException::new);

            return new ProjectPrincipal(user, project);
        }
        throw new UsernameNotFoundException(username);
    }
}
