package org.smarti18n.messages.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import org.smarti18n.messages.projects.ProjectEntity;
import org.smarti18n.messages.projects.ProjectRepository;

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
        final Optional<ProjectEntity> byMail = this.projectRepository.findById(username);
        if (byMail.isPresent()) {
            return new ProjectPrincipal(byMail.get());
        }
        throw new UsernameNotFoundException(username);
    }
}
