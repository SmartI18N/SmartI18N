package org.smarti18n.messages.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.smarti18n.models.Project;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class ProjectPrincipal implements UserDetails {

    static final String ROLE_PROJECT = "PROJECT";

    private final Project project;

    ProjectPrincipal(final Project project) {
        this.project = project;
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
