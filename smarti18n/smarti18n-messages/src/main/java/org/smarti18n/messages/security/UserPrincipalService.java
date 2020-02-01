package org.smarti18n.messages.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import org.smarti18n.messages.users.UserEntity;
import org.smarti18n.messages.users.UserRepository;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
@Component
public class UserPrincipalService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public UserPrincipalService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<UserEntity> byMail = this.userRepository.findByMail(username);
        if (byMail.isPresent()) {
            return new UserPrincipal(byMail.get());
        }
        throw new UsernameNotFoundException(username);

    }
}
