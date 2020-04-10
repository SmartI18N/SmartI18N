package org.smarti18n.messages.security;

import org.smarti18n.models.Project;
import org.smarti18n.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectPrincipal implements UserDetails {

    static final String ROLE_PROJECT = "PROJECT";

    private final User user;
    private final Project project;

    ProjectPrincipal(final User user, final Project project) {
        this.user = user;
        this.project = project;
    }

    public String getProjectId() {
        return project.getId();
    }

    public String getUserMail() {
        return user.getMail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(ROLE_PROJECT));
    }

    @Override
    public String getPassword() {
        return project.getSecret();
    }

    @Override
    public String getUsername() {
        return project.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
