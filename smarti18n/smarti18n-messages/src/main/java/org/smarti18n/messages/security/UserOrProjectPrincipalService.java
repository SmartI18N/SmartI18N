package org.smarti18n.messages.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Component
public class UserOrProjectPrincipalService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserPrincipalService userPrincipalService;
    private final ProjectPrincipalService projectPrincipalService;

    public UserOrProjectPrincipalService(
            final UserPrincipalService userPrincipalService,
            final ProjectPrincipalService projectPrincipalService) {

        this.userPrincipalService = userPrincipalService;
        this.projectPrincipalService = projectPrincipalService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {
            return userPrincipalService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e1) {
            try {
                return projectPrincipalService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e2) {
                throw new UsernameNotFoundException("no user or project with name [" + username + "] found!");
            }
        }
    }
}
