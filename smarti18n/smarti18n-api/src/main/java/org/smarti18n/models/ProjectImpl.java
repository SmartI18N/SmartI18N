package org.smarti18n.models;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ProjectImpl implements Project {

    private String id;
    private String name;
    private String description;
    private String secret;
    private Set<Locale> locales;
    private Set<User> owners;

    public ProjectImpl() {
    }

    public ProjectImpl(final Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.secret = project.getSecret();
        this.locales = project.getLocales() == null ? new HashSet<>() : new HashSet<>(project.getLocales());
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public Set<Locale> getLocales() {
        return this.locales;
    }

    @Override
    public void setLocales(final Set<Locale> locales) {
        this.locales = locales;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public void setSecret(final String secret) {
        this.secret = secret;
    }

    @Override
    public Set<User> getOwners() {
        return owners;
    }

    @Override
    public void setOwners(final Set<User> owners) {
        this.owners = owners;
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectImpl)) {
            return false;
        }

        final ProjectImpl project = (ProjectImpl) o;

        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
